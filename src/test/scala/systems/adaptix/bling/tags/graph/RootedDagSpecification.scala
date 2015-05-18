package systems.adaptix.bling.tags.graph

import org.specs2.mutable.Specification

/**
 * Created by nkashyap on 5/17/15.
 */
class RootedDagSpecification extends Specification {
  "A RootedDag is instantiated with a DagVertex which is its distinguished root." >> {
    val root = DagVertex("root")
    val rootedDag = new RootedDag(root)
    rootedDag.root mustEqual root
  }

  "The RootedDag \"stronglyConnectedComponents\" method returns a set containing the strongly connected components of the RootedDag instance." >> {
    "The strongly connected components are represented by the set of their vertices." >> {
      val root = DagVertex("root")
      val rootedDag = new RootedDag(root)
      val components = rootedDag.stronglyConnectedComponents
      components must haveSize(1)
      components(0) must contain(root)
    }

    "After the method call, each vertex in the RootedDag should have its state refreshed so that its index and lowLink parameters are once again None." >> {
      val child = DagVertex("child")
      val root = DagVertex("root", Set(child))
      val rootedDag = new RootedDag(root)
      val components = rootedDag.stronglyConnectedComponents
      root.index must beNone
      root.lowLink must beNone
      child.index must beNone
      child.lowLink must beNone
    }

    "Example 1: Root and one child. Two strongly connected components." >> {
      val child = DagVertex("child")
      val root = DagVertex("root", Set(child))
      val rootedDag = new RootedDag(root)
      val components = rootedDag.stronglyConnectedComponents
      components must haveSize(2)
    }

    "Example 2: Root and one child with a back link from the child to the root. One strongly connected component." >> {
      val child = DagVertex("child")
      val root = DagVertex("root", Set(child))
      child addChild root
      val rootedDag = new RootedDag(root)
      val components = rootedDag.stronglyConnectedComponents
      components must haveSize(1)
    }

    "Example 3: Vertices A, B, C, D, E. A is roote with children B and C. B has D and E as children. Five strongly connected components." >> {
      val A = DagVertex("A")
      val B = DagVertex("B")
      val C = DagVertex("C")
      val D = DagVertex("D")
      val E = DagVertex("E")
      A addChildren Set(B, C)
      B addChildren Set(D, E)
      val rootedDag = new RootedDag(A)
      val components = rootedDag.stronglyConnectedComponents
      components must haveSize(5)
    }

    "Example 4: Vertices A, B, C, D, E. A is root, with children B and C. B has D and E as children. D connects back to A. Three strongly connected components." >> {
      val A = DagVertex("A")
      val B = DagVertex("B")
      val C = DagVertex("C")
      val D = DagVertex("D")
      val E = DagVertex("E")
      A addChildren Set(B, C)
      B addChildren Set(D, E)
      D addChild A
      val rootedDag = new RootedDag(A)
      val components = rootedDag.stronglyConnectedComponents
      components must haveSize(3)
    }

    "Example 5: Vertices A, B, C, D, E. A is root with children B and C, B has D and E as children. D has C as a child. C has A as a child. Two strongly connected components." >> {
      val A = DagVertex("A")
      val B = DagVertex("B")
      val C = DagVertex("C")
      val D = DagVertex("D")
      val E = DagVertex("E")
      A addChildren Set(B, C)
      B addChildren Set(D, E)
      D addChild C
      C addChild A
      val rootedDag = new RootedDag(A)
      val components = rootedDag.stronglyConnectedComponents
      components must haveSize(2)
    }

    "Example 6: Vertices A, B, C, D, E. A has B and C as children. B has C and D as children. C has D and E as children. D has C and B as children. Three strongly connected components." >> {
      val A = DagVertex("A")
      val B = DagVertex("B")
      val C = DagVertex("C")
      val D = DagVertex("D")
      val E = DagVertex("E")
      A addChildren Set(B, C)
      B addChildren Set(C, D)
      C addChildren Set(D, E)
      D addChildren Set(B, C)
      val rootedDag = new RootedDag(A)
      val components = rootedDag.stronglyConnectedComponents
      components must haveSize(3)
    }
  }

  "The \"isAcyclic\" method tests whether the graph is indeed acyclic." >> {
    "Example 0: Only one vertex. Acyclic." >> {
      val root = DagVertex("")
      val rootedDag = new RootedDag(root)
      rootedDag.isAcyclic must beTrue
    }

    "Example 1: Root and one child. Acyclic." >> {
      val child = DagVertex("child")
      val root = DagVertex("root", Set(child))
      val rootedDag = new RootedDag(root)
      rootedDag.isAcyclic must beTrue
    }

    "Example 2: Root and one child with a back link from the child to the root. Cyclic." >> {
      val child = DagVertex("child")
      val root = DagVertex("root", Set(child))
      child addChild root
      val rootedDag = new RootedDag(root)
      rootedDag.isAcyclic must beFalse
    }

    "Example 3: Vertices A, B, C, D, E. A is root, with children B and C. B has D and E as children. Acyclic." >> {
      val A = DagVertex("A")
      val B = DagVertex("B")
      val C = DagVertex("C")
      val D = DagVertex("D")
      val E = DagVertex("E")
      A addChildren Set(B, C)
      B addChildren Set(D, E)
      val rootedDag = new RootedDag(A)
      rootedDag.isAcyclic must beTrue
    }

    "Example 4: Vertices A, B, C, D, E. A is root, with children B and C. B has D and E as children. D connects back to A. Cyclic." >> {
      val A = DagVertex("A")
      val B = DagVertex("B")
      val C = DagVertex("C")
      val D = DagVertex("D")
      val E = DagVertex("E")
      A addChildren Set(B, C)
      B addChildren Set(D, E)
      D addChild A
      val rootedDag = new RootedDag(A)
      rootedDag.isAcyclic must beFalse
    }

    "Example 5: Vertices A, B, C, D, E. A is root with children B and C, B has D and E as children. D has C as a child. C has A as a child. Cyclic." >> {
      val A = DagVertex("A")
      val B = DagVertex("B")
      val C = DagVertex("C")
      val D = DagVertex("D")
      val E = DagVertex("E")
      A addChildren Set(B, C)
      B addChildren Set(D, E)
      D addChild C
      C addChild A
      val rootedDag = new RootedDag(A)
      rootedDag.isAcyclic must beFalse
    }

    "Example 6: Vertices A, B, C, D, E. A has B and C as children. B has C and D as children. C has D and E as children. D has C and B as children. Cyclic." >> {
      val A = DagVertex("A")
      val B = DagVertex("B")
      val C = DagVertex("C")
      val D = DagVertex("D")
      val E = DagVertex("E")
      A addChildren Set(B, C)
      B addChildren Set(C, D)
      C addChildren Set(D, E)
      D addChildren Set(B, C)
      val rootedDag = new RootedDag(A)
      rootedDag.isAcyclic must beFalse
    }
  }
}

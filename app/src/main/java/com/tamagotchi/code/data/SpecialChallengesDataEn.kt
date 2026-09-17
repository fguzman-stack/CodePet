package com.tamagotchi.code.data

// English translations keyed by CodingChallenge.id. Option ORDER matches the Spanish source.
object SpecialChallengesDataEn {
    val translations: Map<Int, ChallengeTextsEn> = mapOf(
        101 to ChallengeTextsEn(
            language = "Special Logic",
            title = "Find the Unique Element",
            question = "Given an array of integers where every element appears three times except for one that appears exactly once. Find that unique element using O(1) extra memory and O(N) time.",
            options = listOf(
                "Use a hash table (Hash Map)",
                "Sort the array and search linearly",
                "Use bit counting modulo 3 at each position",
                "XOR all the elements"
            ),
            explanation = "The optimal way to solve this is to count the number of set bits at each position i across all the numbers. If we take this sum modulo 3, we get the bit at position i of the unique number."
        ),
        102 to ChallengeTextsEn(
            language = "Special Logic",
            title = "The Knapsack Problem",
            question = "What is the classic approach to accurately solve the 0/1 Knapsack Problem?",
            options = listOf(
                "Greedy algorithm picking by highest value/weight ratio",
                "Dynamic Programming with a 2D table or 1D vector",
                "Binary Search",
                "Topological Sorting"
            ),
            explanation = "The 0/1 Knapsack problem cannot be solved with a Greedy approach with guaranteed accuracy. It requires Dynamic Programming, evaluating subproblems of smaller capacities and available items."
        ),
        103 to ChallengeTextsEn(
            language = "Special Logic",
            title = "Reverse a Linked List",
            question = "How many pointers are typically needed (at minimum) to iteratively reverse a singly linked list in-place?",
            options = listOf("1", "2", "3", "4"),
            explanation = "You need 3 pointers: prev (to point to the previously processed node), current (the current node) and next (so you don't lose the rest of the list when breaking the link)."
        ),
        104 to ChallengeTextsEn(
            language = "Special Logic",
            title = "CAP Theorem",
            question = "In distributed systems, the CAP Theorem says you can only simultaneously guarantee 2 of 3 properties. Which ones?",
            options = listOf(
                "Consistency, Asynchrony, Partitioning",
                "Consistency, Availability, Partition Tolerance",
                "Control, Accessibility, Precision",
                "Cache, API, Persistence"
            ),
            explanation = "CAP stands for Consistency, Availability, and Partition Tolerance. Because network partitions (P) are inevitable, a system must choose between C and A."
        ),
        105 to ChallengeTextsEn(
            language = "Special Logic",
            title = "Detect a Cycle",
            question = "Which two-pointer algorithm (one fast and one slow) is used to detect whether there is a cycle in a linked list?",
            options = listOf(
                "Dijkstra's Algorithm",
                "Tortoise and Hare Algorithm (Floyd)",
                "Depth-First Search (DFS)",
                "A* Search"
            ),
            explanation = "Floyd's cycle-detection algorithm (Tortoise and Hare) uses two pointers advancing at different speeds. If there is a cycle, they will eventually meet."
        ),
        106 to ChallengeTextsEn(
            language = "Special Logic",
            title = "AVL Trees",
            question = "What condition must always hold in an AVL tree for it to be balanced?",
            options = listOf(
                "All leaf nodes must be at the same level",
                "The height difference between the left and right subtrees of any node must be at most 1",
                "The left subtree must always have more nodes than the right one",
                "Node colors must not repeat red in sequence"
            ),
            explanation = "The balance factor in an AVL tree is strictly the height difference, which cannot exceed 1 or be less than -1."
        )
    )
}

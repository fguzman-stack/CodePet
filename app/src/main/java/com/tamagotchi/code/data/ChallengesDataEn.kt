package com.tamagotchi.code.data

// English translations keyed by CodingChallenge.id. Keep in sync with ChallengesData.kt:
// same entry count, same option ORDER (correctAnswerIndex depends on it).
object ChallengesDataEn {
    val translations: Map<Int, ChallengeTextsEn> = mapOf(
        // ========== KOTLIN ==========
        1 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Immutability in Kotlin",
            question = "What's the main difference between 'val' and 'var'?",
            codeSnippet = null,
            options = listOf(
                "val is mutable, var is immutable",
                "val defines a read-only (immutable) reference, var is mutable",
                "val is a compile-time constant, var is evaluated at runtime",
                "No difference, they're aliases inherited from Java"
            ),
            explanation = "In Kotlin, 'val' defines a read-only variable (its value can't be reassigned), while 'var' defines a standard mutable variable."
        ),
        2 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Safe Calls (Null Safety)",
            question = "This code throws a compile error. How do you fix it to allow safe calls to 'length'?",
            codeSnippet = null,
            options = listOf(
                "Change to: val len = name?.length",
                "Change to: val len = name!!?.length",
                "Change to: val len = name.length()",
                "Kotlin doesn't allow nullable variables of any kind"
            ),
            explanation = "Using the safe call operator '?.' (name?.length) returns the length of the string if it's not null, or 'null' otherwise, avoiding null pointer errors."
        ),
        3 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Scope Functions",
            question = "Which scope function returns the result of the lambda block and uses 'this' as the receiver?",
            codeSnippet = null,
            options = listOf(
                "apply",
                "also",
                "run",
                "let"
            ),
            explanation = "'run' executes the block using 'this' as the call receiver and returns the result of the last expression inside the lambda block."
        ),
        12 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Data Class",
            question = "What does the 'data' keyword automatically generate in a class?",
            codeSnippet = null,
            options = listOf(
                "Only getters and setters",
                "equals(), hashCode(), toString(), copy() and componentN()",
                "A no-argument constructor",
                "Serializable implementation"
            ),
            explanation = "'data class' types automatically generate equals(), hashCode(), toString(), copy(), and componentN() functions for destructuring."
        ),
        13 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Elvis Operator",
            question = "What does this code print?",
            codeSnippet = "val name: String? = null\nprintln(name ?: \"Guest\")",
            options = listOf(
                "null",
                "Guest",
                "name",
                "Compile error"
            ),
            explanation = "The Elvis operator '?:' returns the left side if it's not null, or the right side if the left one is null. Here name is null, so it prints 'Guest'."
        ),
        14 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Extension Functions",
            question = "What does an extension function let you do in Kotlin?",
            codeSnippet = null,
            options = listOf(
                "Modify a sealed class from another class",
                "Add new functionality to a class without inheriting from it",
                "Create a function that only runs in .kt file extensions",
                "Extend the runtime of a recursive function"
            ),
            explanation = "Extension functions let you add new methods to an existing class without modifying its source code or inheriting from it."
        ),
        15 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Null Safety in Lists",
            question = "How do you safely get the first element of a nullable list?",
            codeSnippet = null,
            options = listOf(
                "val first = items?.firstOrNull()",
                "val first = items!!.first()",
                "val first = items.first()",
                "val first = items?.get(0)"
            ),
            explanation = "'items?.firstOrNull()' is the safest form: if items is null, firstOrNull() is never called and the result is null. If it's not null, it returns the first element, or null if the list is empty."
        ),
        16 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Coroutines - Dispatchers",
            question = "What is the purpose of Dispatchers.IO in Kotlin coroutines?",
            codeSnippet = null,
            options = listOf(
                "Run UI update tasks",
                "Run I/O operations in the background (files, network, DB)",
                "Run code sequentially on the main thread",
                "It's not a real dispatcher, just a theoretical concept"
            ),
            explanation = "Dispatchers.IO is optimized for input/output operations such as file reads, network calls, or database queries."
        ),
        17 to ChallengeTextsEn(
            language = "Kotlin",
            title = "When Expression",
            question = "How does 'when' differ from Java's 'switch'?",
            codeSnippet = null,
            options = listOf(
                "when only works with integers",
                "when can be used as an expression (returns a value) and doesn't need break",
                "when requires a mandatory default block",
                "when only works with String types"
            ),
            explanation = "'when' in Kotlin is more powerful than 'switch': it can be used as an expression that returns values, doesn't need break, and supports any kind of condition."
        ),
        18 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Sealed Class",
            question = "What is a sealed class used for in Kotlin?",
            codeSnippet = null,
            options = listOf(
                "To hide a class's implementation details",
                "To restrict the inheritance hierarchy to a fixed set of known subtypes",
                "To prevent any instance of the class from being created",
                "To mark a class as deprecated and discouraged"
            ),
            explanation = "'sealed class' types define a restricted inheritance hierarchy: all direct subtypes must be declared in the same module and package (since Kotlin 1.5), which enables exhaustive when."
        ),
        19 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Smart Cast",
            question = "Why does this code compile without an explicit cast?",
            codeSnippet = null,
            options = listOf(
                "Because Kotlin infers that obj is Object and Object has length",
                "Because Kotlin applies a smart cast: after checking 'is String', it treats obj as a String",
                "Because the compiler converts Any to String automatically",
                "It doesn't compile, it needs (obj as String).length"
            ),
            explanation = "Kotlin performs 'smart cast': when you check that a variable is of a certain type with 'is', the compiler automatically treats it as that type within the block."
        ),
        20 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Flow vs LiveData",
            question = "What advantage does a Kotlin Flow have over LiveData?",
            codeSnippet = null,
            options = listOf(
                "Flow is tied to the Activity lifecycle",
                "Flow is reactive without Android dependency imports and has operators like map, filter",
                "Flow only works in Java",
                "LiveData can no longer be used in Kotlin"
            ),
            explanation = "Flow is part of Kotlin (not Android), so it's platform-agnostic and offers functional operators like map, filter, transform, etc."
        ),
        21 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Immutable Collections",
            question = "What's the difference between listOf() and mutableListOf()?",
            codeSnippet = null,
            options = listOf(
                "Both create identical lists, only the name changes",
                "listOf() creates a read-only list, mutableListOf() allows modifying elements",
                "listOf() throws an error if the list has more than 10 elements",
                "mutableListOf() doesn't allow null elements"
            ),
            explanation = "listOf() returns an immutable (read-only) List, while mutableListOf() returns a MutableList that allows adding, removing, and modifying elements."
        ),
        22 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Inline Functions",
            question = "What is the main benefit of 'inline' functions in Kotlin?",
            codeSnippet = null,
            options = listOf(
                "They allow multiple inheritance",
                "They eliminate the overhead of creating lambda objects by copying the code to the call site",
                "They make functions run on a separate thread",
                "They make functions globally visible"
            ),
            explanation = "'inline' copies the function body directly at the call site, eliminating lambda object creation and reducing memory overhead."
        ),
        23 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Try as an Expression",
            question = "What value does this code print?",
            codeSnippet = null,
            options = listOf(
                "Compile error",
                "10",
                "0",
                "\"10\""
            ),
            explanation = "'try' in Kotlin is an expression and returns the value of the last block executed. '10'.toInt() succeeds, so result = 10."
        ),
        24 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Operator Overloading",
            question = "How do you overload the '+' operator for a class in Kotlin?",
            codeSnippet = null,
            options = listOf(
                "By defining a method called add()",
                "By defining an 'operator fun plus()'",
                "By using the @Overload annotation",
                "Operators can't be overloaded in Kotlin"
            ),
            explanation = "In Kotlin, operators are overloaded by implementing functions marked with 'operator': plus() for '+', minus() for '-', times() for '*', etc."
        ),
        25 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Scope Functions - also",
            question = "What makes 'also' different from 'apply'?",
            codeSnippet = null,
            options = listOf(
                "also uses 'this', apply uses 'it'",
                "also uses 'it' as the receiver, apply uses 'this'",
                "also is synchronous, apply is asynchronous",
                "No difference, they're aliases"
            ),
            explanation = "'apply' uses 'this' (implicit receiver) to configure objects, while 'also' uses 'it' (explicit parameter) and is usually used for side effects."
        ),
        86 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Object Keyword",
            question = "What is the 'object' keyword used for in Kotlin?",
            codeSnippet = null,
            options = listOf(
                "Only for creating new objects of an existing class",
                "To declare a singleton class (a single global instance)",
                "It's the equivalent of 'new' in Java",
                "To mark a method as deprecated"
            ),
            explanation = "'object' declares a singleton class: only one instance of it exists, created lazily and safely on first access."
        ),
        87 to ChallengeTextsEn(
            language = "Kotlin",
            title = "Lambda and fold",
            question = "What result does this operation produce?",
            codeSnippet = null,
            options = listOf(
                "10",
                "0",
                "1234",
                "Compile error"
            ),
            explanation = "fold(0) iterates with an initial value of 0: 0+1=1, 1+2=3, 3+3=6, 6+4=10. The result is the total sum, 10."
        ),

        // ========== JAVASCRIPT ==========
        4 to ChallengeTextsEn(
            language = "JavaScript",
            title = "Weird Data Types",
            question = "What does the expression 'typeof null' return in JavaScript?",
            codeSnippet = null,
            options = listOf(
                "\"null\"",
                "\"undefined\"",
                "\"object\"",
                "\"string\""
            ),
            explanation = "Historically in JavaScript, 'typeof null' returns '\"object\"'. It's considered a mistake in the original design of the language, but it hasn't been changed for compatibility."
        ),
        5 to ChallengeTextsEn(
            language = "JavaScript",
            title = "Strict Comparison",
            question = "Why does the strict comparison '0 === false' evaluate to 'false'?",
            codeSnippet = null,
            options = listOf(
                "Because === performs automatic type coercion",
                "Because === compares both the value and the type without coercion",
                "Because 0 is not a falsy value",
                "The JS compiler has a bug with numbers"
            ),
            explanation = "The '==' operator converts both sides to a common type (coercion), so 0 and false match. The '===' operator is strict and does no coercion, catching that Number is not Boolean."
        ),
        6 to ChallengeTextsEn(
            language = "JavaScript",
            title = "Closures",
            question = "What is a Closure in JavaScript?",
            codeSnippet = null,
            options = listOf(
                "A function that closes the browser window",
                "The process of compressing code files for production",
                "The combination of a function and the lexical environment in which it was declared",
                "A reserved method for destroying local variables"
            ),
            explanation = "A closure gives an inner function access to its outer function's scope, even after the outer function has finished executing."
        ),
        26 to ChallengeTextsEn(
            language = "JavaScript",
            title = "let vs var",
            question = "What's the main difference between 'let' and 'var'?",
            codeSnippet = null,
            options = listOf(
                "No difference, let is just a modern alternative",
                "let has block scope, var has function scope",
                "var can't be used in modern browsers",
                "let only works inside objects"
            ),
            explanation = "'let' has block scope (it only exists inside {}), while 'var' has function scope and gets hoisted."
        ),
        27 to ChallengeTextsEn(
            language = "JavaScript",
            title = "Promises - then",
            question = "What does the following code print?",
            codeSnippet = null,
            options = listOf(
                "1",
                "2",
                "undefined",
                "Promise {<pending>}"
            ),
            explanation = "Promise.resolve(1) creates a promise with the value 1. The first then adds 1 (result 2). The second then prints 2."
        ),
        28 to ChallengeTextsEn(
            language = "JavaScript",
            title = "Spread Operator",
            question = "What does the '...' (spread) operator do on an array?",
            codeSnippet = null,
            options = listOf(
                "Concatenates all the arrays in the application",
                "Expands an array into its individual elements",
                "Removes the last element of the array",
                "Creates an empty copy of the array"
            ),
            explanation = "The spread operator '...' expands an iterable (like an array) into its individual elements, useful for copying or combining arrays."
        ),
        29 to ChallengeTextsEn(
            language = "JavaScript",
            title = "Template Literals",
            question = "How do you interpolate a variable into a string with template literals?",
            codeSnippet = null,
            options = listOf(
                "\"Hello \" + name + \"!\"",
                "`Hello \${name}!`",
                "'Hello {name}!'",
                "\"Hello %name%!\""
            ),
            explanation = "Template literals use backticks ` and the \${} syntax to interpolate variables or expressions directly into the string."
        ),
        30 to ChallengeTextsEn(
            language = "JavaScript",
            title = "Arrow Functions",
            question = "What distinguishes an arrow function from a traditional function?",
            codeSnippet = null,
            options = listOf(
                "It can't have parameters",
                "It doesn't have its own 'this', inheriting it from the parent scope",
                "It only works with numbers",
                "It can't return values"
            ),
            explanation = "Arrow functions don't have their own 'this', inheriting it from the surrounding scope. They also lack 'arguments' and can't be used as constructors."
        ),
        31 to ChallengeTextsEn(
            language = "JavaScript",
            title = "Hoisting with var",
            question = "What does this code return?",
            codeSnippet = null,
            options = listOf(
                "5",
                "undefined",
                "ReferenceError: x is not defined",
                "null"
            ),
            explanation = "With 'var', the declaration is hoisted to the top, but the assignment isn't. When console.log runs, x is declared but its value is undefined."
        ),
        32 to ChallengeTextsEn(
            language = "JavaScript",
            title = "Destructuring Assignment",
            question = "What does destructuring do with arrays?",
            codeSnippet = null,
            options = listOf(
                "Removes unwanted elements from the array",
                "Extracts values from the array into individual variables with a concise syntax",
                "Encrypts the array's data for security",
                "Converts the array into an object"
            ),
            explanation = "Destructuring lets you extract values from arrays or objects into individual variables: const [a, b] = [1, 2] assigns 1 to 'a' and 2 to 'b'."
        ),
        33 to ChallengeTextsEn(
            language = "JavaScript",
            title = "map method",
            question = "What does the array 'map' method return?",
            codeSnippet = null,
            options = listOf(
                "A new array with the same number of elements, transformed",
                "A new array with only the elements that meet a condition",
                "The first element that meets a condition",
                "A boolean indicating whether all elements meet the condition"
            ),
            explanation = "'map' iterates over each element and applies a function, returning a new array of the same length with the transformed values."
        ),
        34 to ChallengeTextsEn(
            language = "JavaScript",
            title = "Event Loop",
            question = "In what order are these messages printed?",
            codeSnippet = null,
            options = listOf(
                "A, B, C",
                "B, A, C",
                "A, C, B",
                "C, B, A"
            ),
            explanation = "A and C are synchronous (they run first). setTimeout, with a 0 delay, goes to the task queue and runs after the synchronous stack completes."
        ),
        35 to ChallengeTextsEn(
            language = "JavaScript",
            title = "Falsy Values",
            question = "How many of these values are falsy in JavaScript? 0, '', false, null, undefined, NaN, []",
            codeSnippet = null,
            options = listOf(
                "All of them are falsy",
                "6, except [] (empty arrays are truthy)",
                "4: 0, '', false, null",
                "Only 0 and false"
            ),
            explanation = "The falsy values are: 0, '' (empty string), false, null, undefined, NaN (6 in total). [] (empty array) is truthy."
        ),
        36 to ChallengeTextsEn(
            language = "JavaScript",
            title = "Fetch API",
            question = "How is the response of a fetch call handled?",
            codeSnippet = null,
            options = listOf(
                "fetch directly returns the JSON",
                "fetch returns a Promise that resolves to a Response object",
                "fetch is synchronous and blocks the thread",
                "fetch only works with local files"
            ),
            explanation = "fetch() returns a Promise that resolves to a Response object. To get the JSON, you must call response.json() (also a Promise)."
        ),
        37 to ChallengeTextsEn(
            language = "JavaScript",
            title = "Implicit Coercion",
            question = "What does '5' - 3 return in JavaScript?",
            codeSnippet = null,
            options = listOf(
                "\"53\"",
                "2",
                "\"5-3\"",
                "TypeError error"
            ),
            explanation = "With the '-' operator, JavaScript converts the string '5' to a number (5) and subtracts 3, giving 2. By contrast, '5' + 3 would give \"53\" due to concatenation."
        ),
        38 to ChallengeTextsEn(
            language = "JavaScript",
            title = "this in a Function",
            question = "What does this code print (in the browser)?",
            codeSnippet = null,
            options = listOf(
                "undefined",
                "The global object (window)",
                "null",
                "The foo object"
            ),
            explanation = "In a normal non-strict function, 'this' inside the function refers to the global object (window in the browser). In strict mode it would be undefined."
        ),
        39 to ChallengeTextsEn(
            language = "JavaScript",
            title = "JSON.stringify",
            question = "What does JSON.stringify() do?",
            codeSnippet = null,
            options = listOf(
                "Converts a JSON string into a JavaScript object",
                "Converts a JavaScript object into a JSON string",
                "Validates whether a string is valid JSON",
                "Minifies a string by removing spaces"
            ),
            explanation = "JSON.stringify() serializes a JavaScript object to its JSON string representation. The opposite is JSON.parse()."
        ),
        40 to ChallengeTextsEn(
            language = "JavaScript",
            title = "null vs undefined",
            question = "What's the difference between null and undefined?",
            codeSnippet = null,
            options = listOf(
                "They're exactly the same, synonyms",
                "undefined means 'not assigned', null means 'intentionally empty'",
                "null only exists in strict mode",
                "undefined is a string, null is an object"
            ),
            explanation = "undefined indicates that a variable was declared but not assigned a value. null is a value intentionally assigned to indicate 'no value'."
        ),

        // ========== PHP ==========
        7 to ChallengeTextsEn(
            language = "PHP",
            title = "Basic Variable Syntax",
            question = "What is the mandatory prefix to declare and use any variable in PHP?",
            codeSnippet = null,
            options = listOf(
                "The percent sign (%)",
                "The dollar sign ($)",
                "The 'var' keyword",
                "No prefix, it's defined like in C++"
            ),
            explanation = "In PHP, every variable must start with the '$' symbol followed by the variable name."
        ),
        8 to ChallengeTextsEn(
            language = "PHP",
            title = "Merging Arrays",
            question = "Which native PHP function is used to combine two indexed arrays preserving values?",
            codeSnippet = "\$array1 = [1, 2];\n\$array2 = [3, 4];\n\$result = ???(\$array1, \$array2);",
            options = listOf(
                "array_combine",
                "array_merge",
                "array_concat",
                "implode"
            ),
            explanation = "'array_merge()' combines the elements of one or more arrays together, so the values of one are appended to the end of the previous one."
        ),
        9 to ChallengeTextsEn(
            language = "PHP",
            title = "Strict Comparison",
            question = "What is the result of '123 == \"123\"' versus '123 === \"123\"' in PHP?",
            codeSnippet = null,
            options = listOf(
                "Both return true",
                "Both return false",
                "The first is true (coercion), the second is false (strict)",
                "The first is false, the second is true"
            ),
            explanation = "Like JS, '==' in PHP performs type coercion (converting the string to a number for the comparison), while '===' strictly checks the type (int vs string)."
        ),
        41 to ChallengeTextsEn(
            language = "PHP",
            title = "Superglobal Variables",
            question = "Which superglobal contains the data sent by a form with the POST method?",
            codeSnippet = null,
            options = listOf(
                "\$_GET",
                "\$_POST",
                "\$_SERVER",
                "\$_REQUEST"
            ),
            explanation = "\$_POST contains the form fields sent with method='post'. Uploaded files don't arrive there: they're available separately in \$_FILES."
        ),
        42 to ChallengeTextsEn(
            language = "PHP",
            title = "foreach with Arrays",
            question = "What is the correct syntax to iterate over an associative array?",
            codeSnippet = null,
            options = listOf(
                "foreach(\$array as \$value)",
                "foreach(\$array as \$key => \$value)",
                "for(\$i = 0; \$i < count(\$array); \$i++)",
                "while(\$item = next(\$array))"
            ),
            explanation = "The foreach(\$array as \$key => \$value) syntax iterates over associative arrays giving access to both the key and the value."
        ),
        43 to ChallengeTextsEn(
            language = "PHP",
            title = "isset vs empty",
            question = "isset() returns true if a variable exists and is not null. What does var_dump(empty(\$var)) print when \$var = 0?",
            codeSnippet = null,
            options = listOf(
                "false because 0 is a valid value",
                "true because 0 is considered an empty value",
                "false because empty only checks whether the variable exists",
                "It throws a type error"
            ),
            explanation = "empty(\$var) returns true for 'empty' values: \"\", 0, \"0\", null, false, array(), and undefined variables. Since \$var = 0, empty() returns true, even though the variable does exist."
        ),
        44 to ChallengeTextsEn(
            language = "PHP",
            title = "Classes and Objects",
            question = "How is a class defined in PHP?",
            codeSnippet = null,
            options = listOf(
                "class Person { }",
                "new class Person() { }",
                "function Person() { }",
                "type Person struct { }"
            ),
            explanation = "In PHP, classes are defined with 'class ClassName { }', similar to other languages like Java or C++."
        ),
        45 to ChallengeTextsEn(
            language = "PHP",
            title = "Inheritance in PHP",
            question = "Which keyword is used to inherit from a parent class?",
            codeSnippet = null,
            options = listOf(
                "implements",
                "extends",
                "inherits",
                "parent"
            ),
            explanation = "PHP uses 'extends' for class inheritance: class Child extends Parent { }. For interfaces, 'implements' is used."
        ),
        46 to ChallengeTextsEn(
            language = "PHP",
            title = "echo vs print_r",
            question = "Which function is more suitable for inspecting the contents of an array?",
            codeSnippet = "\$data = ['a' => 1, 'b' => 2];\necho \$data; // Warning: Array to string conversion\n???",
            options = listOf(
                "echo only works for strings, use print_r(\$data) or var_dump(\$data)",
                "Use echo \$data with double quotes",
                "Arrays can't be inspected in PHP",
                "Use console.log(\$data)"
            ),
            explanation = "echo only prints strings. To see the contents of an array or object, use print_r() or var_dump(), which show the full structure."
        ),
        47 to ChallengeTextsEn(
            language = "PHP",
            title = "Sessions in PHP",
            question = "Which function starts or resumes a session in PHP?",
            codeSnippet = null,
            options = listOf(
                "start_session()",
                "session_start()",
                "begin_session()",
                "init_session()"
            ),
            explanation = "session_start() starts a new session or resumes the existing one based on the session ID (in a cookie or URL)."
        ),
        48 to ChallengeTextsEn(
            language = "PHP",
            title = "include vs require",
            question = "What's the difference between include and require?",
            codeSnippet = null,
            options = listOf(
                "include includes the file, require executes it",
                "require throws a fatal error if the file doesn't exist, include only a warning",
                "No difference, they're aliases",
                "include loads the file asynchronously"
            ),
            explanation = "Both include files, but require produces a fatal error (E_COMPILE_ERROR) if it fails, stopping the script. include only emits a warning (E_WARNING)."
        ),
        49 to ChallengeTextsEn(
            language = "PHP",
            title = "Associative Arrays",
            question = "How do you access the value with the key 'name' in an associative array?",
            codeSnippet = null,
            options = listOf(
                "\$person->name",
                "\$person['name']",
                "\$person{name}",
                "\$person::name"
            ),
            explanation = "Associative arrays in PHP use brackets with the key in quotes: \$person['name']. The arrow '->' is used for object properties."
        ),
        50 to ChallengeTextsEn(
            language = "PHP",
            title = "String Concatenation",
            question = "What is the concatenation operator in PHP?",
            codeSnippet = "\$a = 'Hello ';\n\$b = 'World';\n\$c = ???",
            options = listOf(
                "\$a + \$b",
                "\$a . \$b",
                "\$a & \$b",
                "\$a :: \$b"
            ),
            explanation = "PHP uses the dot (.) as the string concatenation operator: 'Hello ' . 'World' produces 'Hello World'."
        ),
        51 to ChallengeTextsEn(
            language = "PHP",
            title = "Variable Functions",
            question = "What do you call a function whose name is stored in a variable?",
            codeSnippet = null,
            options = listOf(
                "\$functionName()",
                "call(\$functionName)",
                "invoke(\$functionName)",
                "Not possible in PHP"
            ),
            explanation = "PHP allows calling functions by dynamic name: if \$functionName = 'strlen', then \$functionName('hello') calls strlen('hello')."
        ),
        52 to ChallengeTextsEn(
            language = "PHP",
            title = "Static Methods",
            question = "How do you access a static method from inside the same class?",
            codeSnippet = null,
            options = listOf(
                "\$this->method()",
                "self::method()",
                "static.method()",
                "class::method()"
            ),
            explanation = "Inside a class, static methods and properties are accessed with self:: or static:: (late static binding)."
        ),
        53 to ChallengeTextsEn(
            language = "PHP",
            title = "PDO and Prepared Statements",
            question = "What is the main benefit of prepared statements in PDO?",
            codeSnippet = null,
            options = listOf(
                "They're faster than regular queries",
                "They prevent SQL injection by separating the SQL structure from the data",
                "They allow connecting multiple databases at once",
                "They don't require a database connection"
            ),
            explanation = "Prepared statements send the query structure separately from the data, preventing malicious data from altering the intent of the SQL."
        ),
        54 to ChallengeTextsEn(
            language = "PHP",
            title = "Cookies in PHP",
            question = "Which function sets a cookie in PHP?",
            codeSnippet = "// What is the correct function?\n???('user', 'john', time() + 3600)",
            options = listOf(
                "set_cookie()",
                "setcookie()",
                "cookie_set()",
                "http_set_cookie()"
            ),
            explanation = "setcookie() is PHP's native function for setting cookies. It must be called before any HTML output."
        ),
        55 to ChallengeTextsEn(
            language = "PHP",
            title = "Ternary Operator",
            question = "What is the syntax of the ternary operator in PHP?",
            codeSnippet = null,
            options = listOf(
                "cond ? if_true : if_false",
                "cond :: if_true :: if_false",
                "if cond then if_true else if_false",
                "cond -> if_true -> if_false"
            ),
            explanation = "PHP uses the same syntax as C/Java: condition ? value_if_true : value_if_false."
        ),

        // ========== PYTHON ==========
        10 to ChallengeTextsEn(
            language = "Python",
            title = "Mutable Data Types",
            question = "Which of the following Python data structures is IMMUTABLE?",
            codeSnippet = null,
            options = listOf(
                "List [1, 2, 3]",
                "Dictionary {'a': 1}",
                "Tuple (1, 2, 3)",
                "Set {1, 2, 3}"
            ),
            explanation = "In Python, tuples '(1, 2, 3)' are immutable. Once created, their elements can't be modified, added, or removed."
        ),
        11 to ChallengeTextsEn(
            language = "Python",
            title = "List Comprehension",
            question = "What is the result of the following list comprehension?",
            codeSnippet = "numbers = [1, 2, 3, 4]\nsquares = [x**2 for x in numbers if x % 2 == 0]",
            options = listOf(
                "[1, 4, 9, 16]",
                "[4, 16]",
                "[1, 9]",
                "[2, 4]"
            ),
            explanation = "The loop filters the even numbers (2 and 4) using 'if x % 2 == 0', then computes their squares (2**2 = 4 and 4**2 = 16), giving [4, 16]."
        ),
        56 to ChallengeTextsEn(
            language = "Python",
            title = "PEP 8 - Naming",
            question = "What is the PEP 8 convention for naming functions and variables in Python?",
            codeSnippet = null,
            options = listOf(
                "camelCase",
                "snake_case",
                "PascalCase",
                "kebab-case"
            ),
            explanation = "PEP 8 recommends using snake_case (words_separated_by_underscores) for functions, variables, and methods. PascalCase for classes."
        ),
        57 to ChallengeTextsEn(
            language = "Python",
            title = "Lists vs Tuples",
            question = "Why are tuples faster than lists in certain contexts?",
            codeSnippet = null,
            options = listOf(
                "Tuples are stored on disk, not in memory",
                "Tuples are immutable, so Python can optimize their storage and access",
                "Tuples don't have methods, only global functions",
                "Tuples are compiled to machine code"
            ),
            explanation = "Being immutable, Python can apply memory optimizations to tuples. They don't need extra space for potential modifications."
        ),
        58 to ChallengeTextsEn(
            language = "Python",
            title = "with Statement",
            question = "What is the 'with' statement used for in Python?",
            codeSnippet = null,
            options = listOf(
                "To run code blocks in parallel",
                "To manage resources (files, connections) ensuring they're automatically closed",
                "To declare variables inside a temporary scope",
                "It's a synonym for 'if' in modern versions"
            ),
            explanation = "'with' uses context managers to ensure resources are properly cleaned up when leaving the block, such as closing files automatically."
        ),
        59 to ChallengeTextsEn(
            language = "Python",
            title = "Mutable as Default",
            question = "What problem does this function have?",
            codeSnippet = "def add_item(item, items=[]):\n    items.append(item)\n    return items",
            options = listOf(
                "No problem, it works correctly",
                "The mutable default argument is shared across all calls",
                "append doesn't exist for lists",
                "You can't assign a default to a parameter"
            ),
            explanation = "Default arguments are evaluated once when the function is defined. If the default is mutable, all calls share the same list."
        ),
        60 to ChallengeTextsEn(
            language = "Python",
            title = "args and kwargs",
            question = "What does *args do in a Python function?",
            codeSnippet = null,
            options = listOf(
                "Converts the arguments into a NumPy array",
                "Allows passing a variable number of positional arguments as a tuple",
                "Indicates the function is private",
                "Multiplies the argument values"
            ),
            explanation = "*args collects the extra positional arguments into a tuple. **kwargs collects keyword arguments into a dictionary."
        ),
        61 to ChallengeTextsEn(
            language = "Python",
            title = "Decorators",
            question = "What is a decorator in Python?",
            codeSnippet = null,
            options = listOf(
                "A function that modifies the behavior of another function",
                "A special method like __init__",
                "A class that extends system functionality",
                "A syntax for declaring constant variables"
            ),
            explanation = "A decorator is a function that takes another function and extends its behavior without modifying its source code, using @decorator_name."
        ),
        62 to ChallengeTextsEn(
            language = "Python",
            title = "range in Loops",
            question = "What does this code print?",
            codeSnippet = null,
            options = listOf(
                "1 2 3",
                "0 1 2",
                "0 1 2 3",
                "1 2"
            ),
            explanation = "range(3) generates the numbers 0, 1, 2 (exclusive stop). By default it starts at 0. It prints '0 1 2'."
        ),
        63 to ChallengeTextsEn(
            language = "Python",
            title = "Dictionaries",
            question = "How do you get a value from a dictionary safely (without KeyError)?",
            codeSnippet = null,
            options = listOf(
                "dict.value('key')",
                "dict.get('key', default)",
                "dict['key'] with a mandatory try-except",
                "dict.fetch('key')"
            ),
            explanation = "dict.get('key', default) returns the value if the key exists, or the default (None if not specified) without raising an exception."
        ),
        64 to ChallengeTextsEn(
            language = "Python",
            title = "F-Strings",
            question = "What is the correct f-string syntax?",
            codeSnippet = null,
            options = listOf(
                "f'Hello {name}'",
                "F('Hello %s', name)",
                "'Hello {name}'.format(name)",
                "printf('Hello', name)"
            ),
            explanation = "f-strings (Python 3.6+) are written with the 'f' or 'F' prefix and use { } to interpolate variables or expressions."
        ),
        65 to ChallengeTextsEn(
            language = "Python",
            title = "Exceptions",
            question = "What is the correct syntax for catching an exception?",
            codeSnippet = null,
            options = listOf(
                "try { } catch (e) { }",
                "try: ... except Exception as e: ...",
                "try: ... catch (e): ...",
                "attempt: ... except: ..."
            ),
            explanation = "Python uses 'try: ... except ErrorType as e: ...' for exception handling. It doesn't use braces, but indentation."
        ),
        66 to ChallengeTextsEn(
            language = "Python",
            title = "Multiple Inheritance",
            question = "Does Python support multiple inheritance?",
            codeSnippet = null,
            options = listOf(
                "No, Python doesn't allow multiple inheritance",
                "Yes, class Child(Parent1, Parent2):",
                "Only if you use the @multiple decorator",
                "Yes, but only with abstract classes"
            ),
            explanation = "Python supports multiple inheritance: class Child(Parent1, Parent2):. The MRO (Method Resolution Order) determines the lookup order."
        ),
        67 to ChallengeTextsEn(
            language = "Python",
            title = "Generators and yield",
            question = "What distinguishes a generator from a regular function?",
            codeSnippet = null,
            options = listOf(
                "Generators can't have parameters",
                "Generators use yield and return an iterator that produces values on demand",
                "Generators only work with integers",
                "No difference, they're the same"
            ),
            explanation = "A generator uses 'yield' instead of 'return' and produces a sequence of values that can be iterated over, keeping its state between calls."
        ),
        68 to ChallengeTextsEn(
            language = "Python",
            title = "Shallow Copies",
            question = "What happens when you modify a list inside a copy made with list()?",
            codeSnippet = null,
            options = listOf(
                "1 (the copy is independent)",
                "99 (list() makes a shallow copy, the inner elements are shared)",
                "Error: nested lists can't be copied",
                "None"
            ),
            explanation = "list() makes a shallow copy. The inner lists are the same objects, so modifying one affects the other."
        ),
        69 to ChallengeTextsEn(
            language = "Python",
            title = "Sets in Python",
            question = "What characteristic defines a set in Python?",
            codeSnippet = null,
            options = listOf(
                "It keeps insertion order and allows duplicates",
                "It doesn't allow duplicate elements and has no defined order",
                "It can only contain strings",
                "It's the same as a list but with extra methods"
            ),
            explanation = "A set is an unordered collection of unique elements. It's used for membership tests and set operations (union, intersection)."
        ),
        70 to ChallengeTextsEn(
            language = "Python",
            title = "Modules and Packages",
            question = "What file does a directory need to be considered a package in Python?",
            codeSnippet = null,
            options = listOf(
                "package.json",
                "__init__.py",
                "main.py",
                "setup.py"
            ),
            explanation = "The __init__.py file (which can be empty) indicates that a directory is a Python package, allowing its modules to be imported."
        ),
        71 to ChallengeTextsEn(
            language = "Python",
            title = "Division in Python 3",
            question = "What is the result of 5 / 2 in Python 3?",
            codeSnippet = "result = 5 / 2\nprint(result)",
            options = listOf(
                "2",
                "2.5",
                "2.0",
                "Error: division not allowed"
            ),
            explanation = "In Python 3, '/' always returns a float (2.5). For integer division use '//' (5 // 2 = 2)."
        ),
        72 to ChallengeTextsEn(
            language = "Python",
            title = "Class Methods",
            question = "How do you define a class method in Python?",
            codeSnippet = null,
            options = listOf(
                "With the @staticmethod decorator",
                "With the @classmethod decorator and 'cls' as the first parameter",
                "With the 'class' keyword inside the method",
                "Class methods can't be defined"
            ),
            explanation = "@classmethod receives the class (cls) as the first argument, unlike @staticmethod which receives neither cls nor self."
        ),
        73 to ChallengeTextsEn(
            language = "Python",
            title = "zip function",
            question = "What does the zip() function do in Python?",
            codeSnippet = null,
            options = listOf(
                "Compresses files in ZIP format",
                "Combines multiple iterables into tuples, in parallel",
                "Sorts the elements of a list",
                "Converts strings to numbers"
            ),
            explanation = "zip() takes multiple iterables and returns an iterator of tuples, where the i-th tuple contains the i-th element from each iterable."
        ),
        74 to ChallengeTextsEn(
            language = "Python",
            title = "Global Variables",
            question = "How do you modify a global variable inside a function?",
            codeSnippet = null,
            options = listOf(
                "By simply assigning it a new value",
                "Using the 'global' keyword followed by the variable name",
                "Putting 'var' in front of the name",
                "Global variables can't be modified from functions"
            ),
            explanation = "To modify a global variable inside a function, it must be declared with 'global variable_name' before assigning a value to it."
        ),
        75 to ChallengeTextsEn(
            language = "Python",
            title = "Dictionary Comprehension",
            question = "What is the syntax of a dictionary comprehension?",
            codeSnippet = null,
            options = listOf(
                "{k.upper(): v for k, v in dict.items()}",
                "[k: v for k, v in dict.items()]",
                "dict(k.upper(): v for k, v in dict.items())",
                "for k, v in dict.items(): {k: v}"
            ),
            explanation = "Dictionary comprehensions use braces {} with key: value: {key: value for item in iterable}. Similar to lists but with a :."
        ),
        76 to ChallengeTextsEn(
            language = "Python",
            title = "lambda",
            question = "What is a lambda function in Python?",
            codeSnippet = null,
            options = listOf(
                "An anonymous single-expression function",
                "A function that can have multiple lines and decorators",
                "The integer division operator",
                "An async function with await"
            ),
            explanation = "lambda creates anonymous one-line functions: lambda args: expression. It's equivalent to a small, throwaway function."
        ),
        77 to ChallengeTextsEn(
            language = "Python",
            title = "re.match vs re.search",
            question = "What's the difference between re.match() and re.search()?",
            codeSnippet = null,
            options = listOf(
                "match searches the whole string, search only at the start",
                "match only searches at the start of the string, search looks through the whole string",
                "Both do the same thing, match is obsolete",
                "match uses regex, search uses exact matching"
            ),
            explanation = "re.match() checks only from the beginning of the string; re.search() scans the whole string for the first match."
        ),
        78 to ChallengeTextsEn(
            language = "Python",
            title = "assert in Python",
            question = "What does the 'assert' statement do?",
            codeSnippet = null,
            options = listOf(
                "Stops execution if the condition is true",
                "Raises AssertionError if the condition is false, used for debugging",
                "Affirms that a variable exists in the global scope",
                "Declares a variable as constant"
            ),
            explanation = "assert condition, message raises AssertionError with the message if the condition is false. It's used to validate invariants at development time."
        ),
        79 to ChallengeTextsEn(
            language = "Python",
            title = "enumerate in Loops",
            question = "What does the enumerate() function do in a for loop?",
            codeSnippet = null,
            options = listOf(
                "Returns the index and the value of each element",
                "Counts how many elements are in the iterable",
                "Assigns a unique number to each element",
                "Sorts the elements numerically"
            ),
            explanation = "enumerate() returns (index, value) tuples for each element, avoiding the need for a manual counter variable."
        ),
        80 to ChallengeTextsEn(
            language = "Python",
            title = "Mutable vs Immutable",
            question = "What does this code print?",
            codeSnippet = null,
            options = listOf(
                "[1, 2, 3]",
                "[1, 2, 3, 4]",
                "[4]",
                "Error: lists can't be assigned"
            ),
            explanation = "b = a doesn't copy the list; b references the same list as a. Modifying a affects b. To copy, use a.copy() or list(a)."
        ),
        81 to ChallengeTextsEn(
            language = "Python",
            title = "sys.argv",
            question = "What does sys.argv contain?",
            codeSnippet = null,
            options = listOf(
                "The command-line arguments passed to the script",
                "The system environment variables",
                "The currently imported modules",
                "The Python search paths"
            ),
            explanation = "sys.argv is a list with the command-line arguments. sys.argv[0] is the script name, sys.argv[1:] are the arguments."
        ),
        82 to ChallengeTextsEn(
            language = "Python",
            title = "Python and JSON",
            question = "Which function converts a JSON string into a Python object?",
            codeSnippet = null,
            options = listOf(
                "json.stringify()",
                "json.loads()",
                "json.parse()",
                "json.decode()"
            ),
            explanation = "json.loads() (load string) converts a JSON string into a Python object. json.dumps() does the reverse operation (object to string)."
        ),
        83 to ChallengeTextsEn(
            language = "Python",
            title = "Inheritance and super()",
            question = "What is super() used for in Python?",
            codeSnippet = null,
            options = listOf(
                "To call the parent class version of a method",
                "To declare a class as higher in the hierarchy",
                "To improve method performance",
                "super() doesn't exist in Python"
            ),
            explanation = "super() returns a proxy object that delegates method calls to the parent class, following the MRO (Method Resolution Order)."
        ),
        84 to ChallengeTextsEn(
            language = "Python",
            title = "Virtualenv",
            question = "What is the purpose of a virtual environment (virtualenv)?",
            codeSnippet = null,
            options = listOf(
                "To create a virtual machine to run Python",
                "To isolate a Python project's dependencies from the global system",
                "To simulate a production environment locally",
                "To speed up Python code execution"
            ),
            explanation = "A virtual environment isolates each Python project's dependencies, avoiding conflicts between library versions."
        ),
        85 to ChallengeTextsEn(
            language = "Python",
            title = "Type Hints",
            question = "How do you indicate a function's return type with type hints?",
            codeSnippet = null,
            options = listOf(
                "def sum(a: int, b: int) -> int:",
                "def sum(int a, int b) returns int:",
                "def sum(a: int, b: int) :: int",
                "@typedef sum(a: int, b: int) -> int"
            ),
            explanation = "Type hints use ': type' for parameters and '-> type' for the return: def sum(a: int, b: int) -> int:"
        ),
        88 to ChallengeTextsEn(
            language = "Python",
            title = "Exception in Division",
            question = "Which exception does Python raise when dividing by zero?",
            codeSnippet = "result = 10 / 0",
            options = listOf(
                "ValueError",
                "ZeroDivisionError",
                "TypeError",
                "ArithmeticError (generic)"
            ),
            explanation = "Python raises ZeroDivisionError when you try to divide by zero. It's a subtype of ArithmeticError."
        ),
        89 to ChallengeTextsEn(
            language = "Python",
            title = "Packages with pip",
            question = "Which command installs a package from PyPI?",
            codeSnippet = null,
            options = listOf(
                "python get package_name",
                "pip install package_name",
                "pip download package_name",
                "python install package_name"
            ),
            explanation = "pip install package_name downloads and installs the package from PyPI (Python Package Index)."
        ),
        90 to ChallengeTextsEn(
            language = "Python",
            title = "asyncio",
            question = "Which keyword is used to define an async function in Python?",
            codeSnippet = null,
            options = listOf(
                "async def",
                "async function",
                "coroutine def",
                "def async"
            ),
            explanation = "Async functions are defined with 'async def'. Inside them, 'await' is used to call other async functions."
        ),
    )
}

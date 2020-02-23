# Collada parser

Simple Collada parser.

```kotlin
Parser(YourParserListener()).parse("/you/input/file.dae")
```

Example of parser: 

```kotlin
Parser(MiniGdxFile("output.3d")).parse("/you/input/file.dae")
```

You can use it as a plugin in your gradle build: 

```kotlin
plugins {
    // ...
    id("com.github.dwursteisen.collada") version "<VERSION>"
}

// ...
collada {
    create("assets") {
        this.daeDirectory.set(project.file("src/main/resources/"))
        this.target.set(project.buildDir)
    }
}
```

Then run the task `collada`.

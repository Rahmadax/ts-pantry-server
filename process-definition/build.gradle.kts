plugins {
    id("drc-workflow.kotlin-conventions")
}

// Process definitions
sourceSets {
    val main by getting
    main.resources.srcDirs("src/main/processes")
}

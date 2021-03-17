# IntellijViolatPlugin
 
---
#### This project automates linearizability violation detection in Java ADTs into Intellij IDE using [Violat](https://github.com/michael-emmi/violat)
 
---
![Build](https://github.com/alaukiknpant/intellijViolatPlugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)


<!-- Plugin description -->

## About Linearizability

[Linearizability](https://cs.brown.edu/~mph/HerlihyW90/p463-herlihy.pdf) is an important correctness property for concurrent objects. Linearizability requires that the
result rendered by concurrently invoked operations from multiple threads/processes is equivalent to the result rendered by
a schedule of operations performed sequentially. Verifying the Linearizability of the series of operations associated with an Abstract Data Type
is NP-complete. Violat is a tool developed by [Micheal Emmi](https://michael-emmi.github.io/) that generates tests that witness violations to atomicity and
reliably expose ADT-consistency violations.[[1]](https://link.springer.com/chapter/10.1007/978-3-030-25543-5_30) We present an Intellij Plugin
built on top of violat to fully automate the process of detecting linearizability violations.


## Requirements

The requirements for the plugin are the same as that of Violat

* [Node.js](https://nodejs.org/en/) runtime for JavaScript: version 10.0 or greater
* [Java SE Development Kit](http://www.oracle.com/technetwork/java/javase): version 8
* [Gradle](https://gradle.org/) build tool: at least version 6
* [Maven](https://maven.apache.org/) project management tool
* [Java Pathfinder](https://github.com/javapathfinder) available in your executable environment (in which JDK 8 is required)

## Test your ADTs for linearizability violations in a Docker container

The runtime environment required for the plugin can be found in [this](https://github.com/alaukiknpant/intellijViolatPlugin/blob/main/violatdocker/Dockerfile)
Dockerfile. Once you download the plugin, you can checkout Intellij's [recources](https://www.jetbrains.com/help/idea/running-a-java-app-in-a-container.html)
on how to run your Java application in a container with a specific runtime environment.


## FAQs

#### 1. How to add Java Pathfinder executable to your path

Clone [jpf-core](https://github.com/javapathfinder/jpf-core) and checkout [this](http://javapathfinder.sourceforge.net/Running_JPF.html)
article.



[comment]: <> (## Template ToDo list)

[comment]: <> (- [x] Create a new [IntelliJ Platform Plugin Template][template] project.)

[comment]: <> (- [ ] Verify the [pluginGroup]&#40;/gradle.properties&#41;, [plugin ID]&#40;/src/main/resources/META-INF/plugin.xml&#41; and [sources package]&#40;/src/main/kotlin&#41;.)

[comment]: <> (- [ ] Review the [Legal Agreements]&#40;https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html&#41;.)

[comment]: <> (- [ ] [Publish a plugin manually]&#40;https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate&#41; for the first time.)

[comment]: <> (- [ ] Set the Plugin ID in the above README badges.)

[comment]: <> (- [ ] Set the [Deployment Token]&#40;https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html&#41;.)

[comment]: <> (- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.)


[comment]: <> (This Fancy IntelliJ Platform Plugin is going to be your implementation of the brilliant ideas that you have.)

[comment]: <> (This specific section is a source for the [plugin.xml]&#40;/src/main/resources/META-INF/plugin.xml&#41; file which will be extracted by the [Gradle]&#40;/build.gradle.kts&#41; during the build process.)

[comment]: <> (To keep everything working, do not remove `<!-- ... -->` sections. )

<!-- Plugin description end -->

[comment]: <> (## Installation)

[comment]: <> (- Using IDE built-in plugin system:)

[comment]: <> (  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "intellijViolatPlugin"</kbd> >)

[comment]: <> (  <kbd>Install Plugin</kbd>)

[comment]: <> (- Manually:)

[comment]: <> (  Download the [latest release]&#40;https://github.com/alaukiknpant/intellijViolatPlugin/releases/latest&#41; and install it manually using)

[comment]: <> (  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>)

 
---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
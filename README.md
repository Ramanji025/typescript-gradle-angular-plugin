# TypeScript Gradle Plugin

This plugin makes it easy to build TypeScript projects using Gradle.
Among other things, the plugin provides a task to run the TypeScript compiler.


# Quickstart

This will guide you through the steps needed to set up typescript-gradle-plugin for a TypeScript application project
using Maven/Gradle standard layout.  
You can either use this plugin in combination with the Gradle Node plugin (recommended) or alternatively
use it standalone with a local Node and TypeScript installation.


## Usage with Node plugin

This is the recommended way to use the TypeScript Gradle plugin.
Using the [Node plugin](https://github.com/srs/gradle-node-plugin) has the advantage
that you do not need to have Node or TypeScript installed manually on the system
to execute the TypeScript compile task.
You can define a TypeScript compiler version which gets downloaded automatically.


### Add plugin dependencies

Add a plugin dependency for the Node plugin and for the TypeScript Gradle plugin.

If you are using Gradle 2.1 or later, define the plugin dependency as follows:

	plugins {
	  id "com.moowork.node" version "0.12"
	  id "io.github.ramanji025.gradle.plugin" version "1.0.2"
	}

If you are using Gradle 2.0 or earlier, define the plugin dependency as follows:

	buildscript {
	  repositories {
	    maven {
	      url "https://plugins.gradle.org/m2/"
	    }
	  }
	  dependencies {
	    classpath "com.moowork.gradle:gradle-node-plugin:0.12"
	    classpath "io.github.ramanji025.gradle.plugin:typescript-gradle-plugin:1.0.2"
	  }
	}
	
	apply plugin: 'com.moowork.node'
	apply plugin: 'io.github.ramanji025.gradle.plugin'


### Configure the TypeScript task to use the Node executable

Configure the TypeScript task to use the Node executable as follows:

	import com.moowork.gradle.node.NodeExtension
	import com.moowork.gradle.node.variant.VariantBuilder
	
	node {
	  download = true
	}
	
	String nodeExecutable() {
	  NodeExtension nodeExt = NodeExtension.get(project)
	  return new VariantBuilder(nodeExt).build().nodeExec
	}
	
	compileTypeScript {
	  compilerExecutable "${nodeExecutable()} node_modules/typescript/lib/tsc.js"
	  dependsOn "npmInstall"
	}


### Create package.json with TypeScript version

Create a `package.json` file next to the `build.gradle` file and declare the TypeScript compiler version to use
as follows:

	{ "dependencies": { "typescript": "1.8.7" } }


## Usage with local Node and TypeScript installation

This is not the recommended way of using the plugin. You should prefer to use it with the Node plugin.
But if you have good reasons to do so, here is how...

You need to have installed node.js and installed the typescript node module:

	npm install -g typescript

Alternatively on windows you can install the Typescript SDK and configure the `compilerExecutable` config option to `tsc` - see *Available configuration options*.


### Add plugin dependency

If you are using Gradle 2.1 or later, define the plugin dependency as follows:

	plugins {
	  id "io.github.ramanji025.gradle.plugin" version "1.8.0"
	}


If you are using Gradle 2.0 or earlier, define the plugin dependency as follows:

	buildscript {
	  repositories {
	    maven {
	      url "https://plugins.gradle.org/m2/"
	    }
	  }
	  dependencies {
	    classpath "io.github.ramanji025.gradle.plugin:typescript-gradle-plugin:1.0.2"
	  }
	}
	
	apply plugin: "io.github.ramanji025.gradle.plugin"


## Configuring the TypeScript compile task

You can configure the TypeScript compile task as shown below:

	compileTypeScript {
		sourcemap = true
		// additional configuration options
	}


## Run the TypeScript compiler

	gradle compileTypeScript


# Available configuration options

Here is a list of the available configuration options of the _compileTypeScript_ task:

| Option                             | Type      | Description                                                                                               |
| ---------------------------------- | --------- | --------------------------------------------------------------------------------------------------------- |
| `source`                           | `File`    | directories to compile, defaults to `src/main/ts`                                                         |
| `outputDir`                        | `File`    | the output directory, defaults to _buildDir_/ts                                                           |
| `out`                              | `File`    | DEPRECATED. Use `outFile` instead. |
| `outFile`                          | `File`    | Concatenate and emit output to single file, e.g. `file("${buildDir}/js/out.js")`. The order of concatenation is determined by the list of files passed to the compiler on the command line along with triple-slash references and imports. See output file order documentation for more details. |
| `module`                           | [Module]  | Specify module code generation (`AMD`, `COMMONJS`, `SYSTEM`, `UMD`, `ES6` or `ES2015`) |
| `target`                           | [Target]  | Specify ECMAScript target version (`ES3`, `ES5`, `ES6` or `ES2015`) |
| `declaration`                      | `boolean` | Generates corresponding .d.ts file |
| `noImplicitAny`                    | `boolean` | Warn on expressions and declarations with an implied 'any' type |
| `noResolve`                        | `boolean` | Skip resolution and preprocessing |
| `removeComments`                   | `boolean` | Do not emit comments to output |
| `sourcemap`                        | `boolean` | Generates corresponding .map file |
| `sourceRoot`                       | `File`    | Specifies the location where debugger should locate TypeScript files instead of source locations |
| `codepage`                         | `Integer` | Specify the codepage to use when opening source files |
| `mapRoot`                          | `File`    | Specifies the location where debugger should locate map files instead of generated locations |
| `compilerExecutable`               | `String`  | The tsc compiler executable to use. Defaults to `cmd /c tsc.cmd` on windows and `tsc` on other systems. |
| `noEmitOnError`                    | `boolean` | Do not emit outputs if any type checking errors were reported |
| `noEmit`                           | `boolean` | Do not emit outputs |
| `experimentalDecorators`           | `boolean` | Enables experimental support for ES7 decorators |
| `newline`                          | [Newline] | Specifies the end of line sequence to be used when emitting files (`CRLF` or `LF`) |
| `preserveConstEnums`               | `boolean` | Do not erase const enum declarations in generated code |
| `projectFile`                      | `File`    | Compile the project using the given tsconfig file, or - if a directory is specified - compile the project in the given directory where a tsconfig.json file is present. File specified with the `source` option will be ignore, but you should still explicitly configure the source files as this will make the Gradle UP-TO-DATE check work. |
| `rootDir`                          | `File`    | Specifies the root directory of input files. Use to control the output directory structure with `outDir`. |
| `suppressImplicitAnyIndexErrors`   | `boolean` | Suppress noImplicitAny errors for indexing objects lacking index signatures |
| `noEmitHelpers`                    | `boolean` | Do not emit helpers like `__extends` |
| `inlineSourceMap`                  | `boolean` | Causes source map files to be written inline in the generated .js files instead of in a independent .js.map file |
| `inlineSources`                    | `boolean` | Allows for additionally inlining the source .ts file into the .js file when used in combination with `inlineSourceMap` |
| `watch`                            | `boolean` | Watch input files |
| `charset`                          | `String`  | The character set of the input files |
| `emitBOM`                          | `boolean` | Emit a UTF-8 Byte Order Mark (BOM) in the beginning of output files |
| `emitDecoratorMetadata`            | `boolean` | Emit design-type metadata for decorated declarations in source |
| `isolatedModules`                  | `boolean` | Unconditionally emit imports for unresolved files |
| `jsx`                              | [Jsx]     | Specify JSX code generation (`PRESERVE` or `REACT`) |
| `locale`                           | `String`  | The locale to use to show error messages, e.g. `en-us` |
| `moduleResolution`                 | [ModuleResoltion] | Specify module resolution strategy (`NODE` or `CLASSIC`) |
| `noLib`                            | `boolean` | Do not include the default library file (`lib.d.ts`) |
| `stripInternal`                    | `boolean` | Do not emit declarations for code that has an `/** @internal */` JSDoc annotation |
| `diagnostics`                      | `boolean` | Show diagnostic information. |
| `reactNamespace`                   | `String`  | Specifies the object invoked for createElement and __spread when targeting 'react' JSX emit. |
| `listFiles`                        | `boolean` | Print names of files part of the compilation. |
| `skipDefaultLibCheck`              |           |  |
| `pretty`                           | `boolean` | Stylize errors and messages using color and context. |
| `suppressExcessPropertyErrors`     | `boolean` | Suppress excess property checks for object literals |
| `allowUnusedLabels`                | `boolean` | Do not report errors on unused labels |
| `noImplicitReturns`                | `boolean` | Report error when not all code paths in function return a value |
| `noFallthroughCasesInSwitch`       | `boolean` | Report errors for fallthrough cases in switch statement |
| `allowUnreachableCode`             | `boolean` | Do not report errors on unreachable code |
| `forceConsistentCasingInFileNames` | `boolean` | Disallow inconsistently-cased references to the same file |
| `allowSyntheticDefaultImports`     | `boolean` | Allow default imports from modules with no default export. This does not affect code emit, just typechecking. |
| `allowJs`                          | `boolean` | Allow JavaScript files to be compiled |
| `noImplicitUseStrict`              | `boolean` | Do not emit "use strict" directives in module output |


# Examples

Several example projects can be found in [/examples](examples).


# Integrating the compiled files into a WAR file (for Java Webapps)

If you are integrating TypeScript into a Java web application, you can easily integrate the compiled files into the WAR file.
All you have to do is to configure the war task to pick up the compiled files.
Whenever you call the war task, the TypeScript compiler will compile your TypeScript files first.
In the example below, the compiled files will be put into the js directory in the WAR file.

	apply plugin: "war"
 
	war {
    		into("js") {
        		from compileTypeScript.outputs
    		}
	}


# Configuring multiple source directories

You can configure the TypeScript compile task to use multiple source directories as shown below:

	compileTypeScript {
		source = [file("src/main/ts"), file("src/main/additionalts")]
	}

[ModuleResolution]: typescript-gradle-plugin/src/main/groovy/io/github/ramanji025/gradle/plugins/typescript/ModuleResoltion.groovy
[Jsx]: typescript-gradle-plugin/src/main/groovy/io/github/ramanji025/gradle/plugins/typescript/Jsx.groovy
[Newline]: typescript-gradle-plugin/src/main/groovy/io/github/ramanji025/gradle/plugins/typescript/Newline.groovy
[Target]: typescript-gradle-plugin/src/main/groovy/io/github/ramanji025/gradle/plugins/typescript/Target.groovy
[Module]: typescript-gradle-plugin/src/main/groovy/io/github/ramanji025/gradle/plugins/typescript/Module.groovy

/**
 * Copyright (C) 2023 Ram Pathuri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.ramanji025.gradle.plugin.typescript

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.*

class CompileTypeScript extends SourceTask {

    @OutputDirectory
    @Optional
    File outputDir;
    @OutputFile
    @Optional
    File out
    @OutputFile
    @Optional
    outFile
    @Input
    @Optional
    Module module
    @Input
    @Optional
    Target target
    @Input
    @Optional
    Boolean declaration
    @Input
    @Optional
    Boolean noImplicitAny
    @Input
    @Optional
    Boolean noResolve
    @Input
    @Optional
    Boolean removeComments
    @Input
    @Optional
    Boolean sourcemap
    @InputFile
    @Optional
    File sourceRoot
    @Input
    @Optional
    Integer codepage
    @InputFile
    @Optional
    File mapRoot
    @Input
    @Optional
    Boolean noEmitOnError
    @Input
    @Optional
    Boolean noEmit
    @Input
    @Optional
    Boolean experimentalDecorators
    @Input
    @Optional
    Newline newline
    @Input
    @Optional
    Boolean preserveConstEnums
    @InputFile
    @Optional
    File projectFile
    @InputFile
    @Optional
    File rootDir
    @Input
    @Optional
    Boolean suppressImplicitAnyIndexErrors
    @Input
    @Optional
    Boolean noEmitHelpers
    @Input
    @Optional
    Boolean inlineSourceMap
    @Input
    @Optional
    Boolean inlineSources
    @Input
    @Optional
    Boolean watch
    @Input
    @Optional
    String charset
    @Input
    @Optional
    Boolean emitBOM
    @Input
    @Optional
    Boolean emitDecoratorMetadata
    @Input
    @Optional
    Boolean isolatedModules
    @Input
    @Optional
    Jsx jsx
    @Input
    @Optional
    String locale
    @Input
    @Optional
    ModuleResoltion moduleResolution
    @Input
    @Optional
    Boolean noLib
    @Input
    @Optional
    Boolean stripInternal
    @Input
    @Optional
    Boolean diagnostics
    @Input
    @Optional
    String reactNamespace
    @Input
    @Optional
    Boolean listFiles
    @Input
    @Optional
    Boolean skipDefaultLibCheck
    @Input
    @Optional
    Boolean pretty
    @Input
    @Optional
    Boolean suppressExcessPropertyErrors
    @Input
    @Optional
    Boolean allowUnusedLabels
    @Input
    @Optional
    Boolean noImplicitReturns
    @Input
    @Optional
    Boolean noFallthroughCasesInSwitch
    @Input
    @Optional
    Boolean allowUnreachableCode
    @Input
    @Optional
    Boolean forceConsistentCasingInFileNames
    @Input
    @Optional
    Boolean allowSyntheticDefaultImports
    @Input
    @Optional
    Boolean allowJs
    @Input
    @Optional
    Boolean noImplicitUseStrict
    @Input
    String compilerExecutable = Os.isFamily(Os.FAMILY_WINDOWS) ? "cmd /c tsc.cmd" : "tsc"

    @TaskAction
    void compile() {
        logger.info "compiling TypeScript files..."

        validate()

        File tsCompilerArgsFile = createTsCompilerArgsFile()
        logger.debug("Contents of typescript compiler arguments file: " + tsCompilerArgsFile.text)

        List<String> compilerExecutableAndArgs = compilerExecutable.split(" ").findAll { it.length() > 0 }
        String exe = compilerExecutableAndArgs[0]
        List<String> arguments = compilerExecutableAndArgs.tail() + ('@' + tsCompilerArgsFile)
        project.exec {
            executable = exe
            args = arguments
        }

        logger.info "Done TypeScript compilation."
        if (tsCompilerArgsFile.exists()) {
            tsCompilerArgsFile.delete()
        }
    }

    private File createTsCompilerArgsFile() {
        File tsCompilerArgsFile = File.createTempFile("tsCompiler-", ".args")
        tsCompilerArgsFile.deleteOnExit()

        addFlagsIfPresent(tsCompilerArgsFile, [
                'declaration'                     : declaration == null ? Boolean.FALSE : declaration,
                'noImplicitAny'                   : noImplicitAny == null ? Boolean.FALSE : noImplicitAny,
                'noResolve'                       : noResolve == null ? Boolean.FALSE: noResolve,
                'removeComments'                  : removeComments == null ? Boolean.FALSE : removeComments,
                'sourceMap'                       : sourcemap == null ? Boolean.FALSE : sourcemap,
                'noEmitOnError'                   : noEmitOnError == null ? Boolean.FALSE: noEmitOnError,
                'noEmit'                          : noEmit == null ? Boolean.FALSE: noEmit,
                'experimentalDecorators'          : experimentalDecorators == null ? Boolean.FALSE: experimentalDecorators,
                'preserveConstEnums'              : preserveConstEnums == null ? Boolean.FALSE: preserveConstEnums,
                'suppressImplicitAnyIndexErrors'  : suppressImplicitAnyIndexErrors == null ? Boolean.FALSE: suppressImplicitAnyIndexErrors,
                'noEmitHelpers'                   : noEmitHelpers == null? Boolean.FALSE: noEmitHelpers,
                'inlineSourceMap'                 : inlineSourceMap == null? Boolean.FALSE: inlineSourceMap,
                'inlineSources'                   : inlineSources == null? Boolean.FALSE: inlineSources,
                'watch'                           : watch == null? Boolean.FALSE: watch,
                'emitBOM'                         : emitBOM == null? Boolean.FALSE: emitBOM,
                'emitDecoratorMetadata'           : emitDecoratorMetadata == null? Boolean.FALSE: emitDecoratorMetadata,
                'isolatedModules'                 : isolatedModules == null? Boolean.FALSE: isolatedModules,
                'noLib'                           : noLib == null? Boolean.FALSE: noLib,
                'stripInternal'                   : stripInternal == null ? Boolean.FALSE: stripInternal,
                'diagnostics'                     : diagnostics == null? Boolean.FALSE: diagnostics ,
                'listFiles'                       : listFiles == null? Boolean.FALSE: listFiles,
                'skipDefaultLibCheck'             : skipDefaultLibCheck == null? Boolean.FALSE: skipDefaultLibCheck,
                'pretty'                          : pretty == null? Boolean.FALSE: pretty,
                'suppressExcessPropertyErrors'    : suppressExcessPropertyErrors == null? Boolean.FALSE: suppressExcessPropertyErrors,
                'allowUnusedLabels'               : allowUnusedLabels == null? Boolean.FALSE: allowUnusedLabels,
                'noImplicitReturns'               : noImplicitReturns == null? Boolean.FALSE: noImplicitReturns,
                'noFallthroughCasesInSwitch'      : noFallthroughCasesInSwitch == null? Boolean.FALSE: noFallthroughCasesInSwitch,
                'allowUnreachableCode'            : allowUnreachableCode == null? Boolean.FALSE: allowUnreachableCode,
                'forceConsistentCasingInFileNames': forceConsistentCasingInFileNames == null? Boolean.FALSE: forceConsistentCasingInFileNames,
                'allowSyntheticDefaultImports'    : allowSyntheticDefaultImports == null? Boolean.FALSE: allowSyntheticDefaultImports,
                'allowJs'                         : allowJs == null? Boolean.FALSE: allowJs,
                'noImplicitUseStrict'             : noImplicitUseStrict == null? Boolean.FALSE: noImplicitUseStrict
        ])

        addOptionsIfPresent(tsCompilerArgsFile, [
                'outDir'          : outputDir,
                'out'             : out,
                'outFile'         : outFile,
                'project'         : projectFile,
                'rootDir'         : rootDir,
                'mapRoot'         : mapRoot,
                'sourceRoot'      : sourceRoot,
                'locale'          : locale,
                'charset'         : charset,
                'codepage'        : codepage,
                'module'          : module ? module.name().toLowerCase() : null,
                'target'          : target ? target.name() : null,
                'newLine'         : newline ? newline.name() : null,
                'jsx'             : jsx ? jsx.name().toLowerCase() : null,
                'reactNamespace'  : reactNamespace,
                'moduleResolution': moduleResolution ? moduleResolution.name().toLowerCase() : null
        ])

        addSourceFilesIfPresent(tsCompilerArgsFile, source, projectFile)

        return tsCompilerArgsFile
    }

    private void addSourceFilesIfPresent(File tsCompilerArgsFile, FileTree source, File projectFile) {
        List<String> files = source.collect { File f -> return "\"${f.toString();}\"" };
        logger.debug("TypeScript files to compile: " + files.join(" "));
        if (files) {
            if (projectFile) {
                logger.info("Source provided in combination with projectFile. Source option will be ignored.")
            } else {
                tsCompilerArgsFile.append(" " + files.join(" "))
            }
        }
    }

    private void addFlagsIfPresent(File tsCompilerArgsFile, Map<String, Object> potentialFlags) {
        potentialFlags.each { String flagName, Object flagValue ->
            if (flagValue) {
                tsCompilerArgsFile.append(" --${flagName}")
            }
        }
    }

    private static void addOptionsIfPresent(File tsCompilerArgsFile, Map<String, Object> potentialOptions) {
        for (Map.Entry<Integer, Integer> entry : potentialOptions.entrySet()) {
            String optionName = entry.getKey()
            Object optionValue = entry.getValue()
            if (optionValue) {
                addOption(tsCompilerArgsFile, optionName, optionValue)
            }
        }
    }

    private static void addOption(File tsCompilerArgsFile, String optionName, Object option) {
        if (option instanceof File) {
            tsCompilerArgsFile.append(" --${optionName} \"${option}\"")
        } else {
            tsCompilerArgsFile.append(" --${optionName} ${option}")
        }
    }

    private void validate() {
        if (sourcemap && inlineSourceMap) {
            throw new InvalidUserDataException("Option 'sourcemap' cannot be specified with option 'inlineSourceMap'")
        }
    }
}

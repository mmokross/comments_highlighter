# Comments Highlighter

This plugin allows creating custom highlighting of commented lines on any language. It is possible to add custom
highlight of language keywords in method signatures in Java (for example, highlight "public" keyword). Plugin has
possibility to define custom user tokens for commented line highlighting.

## How to add custom tokens
+ Open settings window
+ Tools->Comments Highlighter Settings
+ Click on "+" sign and enter any token in popup. (Third screenshot of plugin)
+ **Reopen settings window.** This is mandatory step due
  to [bug in Intellij](https://youtrack.jetbrains.com/issue/IDEA-226087)

## Implementation notes

General architecture is simple and has 2 main parts:

+ Annotators:
  + **CommentHighlighterAnnotator**
    implements [Annotator](https://www.jetbrains.org/intellij/sdk/docs/reference_guide/custom_language_support/syntax_highlighting_and_error_highlighting.html#annotator)
    to deal with PsiComment. In such way plugin is able to handle any language, supported by Intellij IDEA.
  + **KeywordHighlighterAnnotator** language specific annotator to highlight keywords of desired language.
+ Highlighters:
  + **CommentHighlighter** main class to define, what part of comment must be highlighted.
  + **KeywordHighlighter** main class to define, what keywords must be highlighted.

## Version history

### 2.0

+ **(Feature)** Add possibility to create custom highlighting for java keyword.
+ **(Feature)** Add default highlight for public keyword.
+ **(Improvement)** Migrate on newest gradle.

### 1.3.6

+ **(Improvement)** Migrate on java 11 toolchain.

### 1.3.5

+ **(Improvement)** Migrate on non-deprecated api usage.
+ **(Improvement)** Disable until version plugin definition.

### 1.3.4

+ **(Bug Fix)** Fix StringIndexOutOfBoundsException on Rider IDE.

### 1.3.3

+ **(Bug Fix)** Made compatible with EAP builds.

### 1.3.2
+ **(Documentation)** Add how-to define custom user token.

### 1.3.1
+ **(Feature)** Add possibility to create custom tokens.
+ **(Bug Fix)** Define plugin compatibility starting from version 2019.1.

### 1.2
+ **(Feature)** Improve performance.
+ **(Feature)** Change default color scheme.

### 1.1
+ **(Bug Fix)** Fix fail positive highlight of xml comments.

### 1.0
+ **(Feature)** Add support for one line, multi line and doc comments highlighting.

# Comments Highlighter
Plugin to highlight comments. Would work for any language, supported by Intellij. Allows to define custom user tokens.

## How to add custom tokens
+ Open settings window
+ Tools->Comments Highlighter Settings
+ Click on "+" sign and enter any token in popup. (Third screenshot of plugin)
+ **Reopen settings window.** This is mandatory step due to [bug in Intellij](https://youtrack.jetbrains.com/issue/IDEA-226087)

## Implementation notes
General architecture is simple and has 2 main parts:
+ **CommentHighlighterAnnotator** implements [Annotator](https://www.jetbrains.org/intellij/sdk/docs/reference_guide/custom_language_support/syntax_highlighting_and_error_highlighting.html#annotator) to deal with PsiComment. In such way plugin is able to handle any language, supported by Intellij IDEA. 
+ **HighlightTextAttributeUtils** main class to define, what part of comment must be highlighted. Is covered by HighlightTextAttributeUtilsTest. 

## Version history
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

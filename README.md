Publo - meaning: small, humble
==============================

*Create great web content using [markdown][markdownspec].*

Publo allows you to generate content freely anywhere you might be.

Write and preview content for your website with no need for an internet connection.

Customise the Look and Feel with an advanced templating system and when you are ready upload it to your hosting.

![Publo](https://raw.githubusercontent.com/AntoCuc/Publo/master/publo.png)

Getting Started
--------------

Publo's main editing function can be accessed via the central Editor.

The markdown written is automatically saved and previewed in the preview area so
you can keep an eye on how the information flows on the site template.

The exported content is fully operational in offline mode so that after exporting
you can also view changes offline with your favourite browser (Chrome, Firefox, etc.).

Publo's utilities are operated via the function keys:  

F1  - Load the about, credits and help system. Also links to this page.  
F2 (When operating the File browser) - Rename files  
F6  - Upload the site  
F7  - Export the site (Find the resulting markup in the project "target" folder)  
F11 - Enter full screen mode (Escape to exit full screen)  

Via key combination:  

Ctrl+Shift+N - Create a new project  

Via mouse:  

Right-click (When operating the file browser) - Create new file or directory  

History
-------

The first version of Publo was built as part of a challenge. A friend commented on Java and Swing complaining about speed of development, especially when a Swing GUI was present.

We bet a fancy coffee that I would be able to write and release a basic markdown editor on my train home (1.5 hours). Needless to say the challenge was accepted. Publo was born. The first few commits show Publo in its infancy.

Since then Publo was ported to JavaFX 2.0.

Features
--------

* Editing markdown documents - Allows you to create great content
* Preview HTML - To view how your content looks before publishing it
* Apply custom templates - Design your very own web UI
* Export - To generate a site ready for upload to your favourite hosting or local browsing
* Run on Windows, OSX and Linux - Because every platform has something special <3

Templates
---------

Publo supports the creation of custom templates via the [thymeleaf template engine][thymeleaf].

Creating a custom template is easy:

1. Create a blank html file - for example: `my-template.html`
2. Place the attribute `th:utext="${main}"` in the tag wrapping your content
3. Save the newly created template file in `~/.publo/<project directory>/templates`
4. To start using the template create a new page metadata item with the "template" key and template file name value.  

Example template reference:

```
---

template: my-template

---
```

Page-level template definitions allow the most flexible user experience customisation.

The templating engine's full documentation can be found on the [thymeleaf official documentation][thymeleafdocs].

### A note on page metadata when using Publo ###

Markdown is a syntax that focuses primarily on webpages main content.
Hence, the use of the `main` attribute for template rendering in Publo.  
However, webpages may contain useful metadata (titles, descriptions, author tags ...).

Publo uses YAML front matter for the definition of such metadata.

Below an example of the syntax for front matter:

```
---

title: Title of the page
description: Description of the page
<key>: <value>

---
```

Populating the template with custom data  can be achieved with `th:utext="${<key>}"`.

Download Publo
--------------

Preview downloads of Publo are available on [GitHub releases](https://github.com/AntoCuc/Publo/releases).

Run Publo
-----------

Running Publo only requires a Java JRE 1.8 or greater.

You can download the version for your favourite operating system from the release tab.

* On Windows -> double-click the executable.
* On Mac -> Move the app to the Applications folder and run using finder or double-click the .app file
* On Linux -> `java -jar publo.jar`

Software Builds
---------------

Updates
-------

[![Dependency Status](https://www.versioneye.com/user/projects/5808c4f0d65a77002f5eab5c/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5808c4f0d65a77002f5eab5c)

If no features are developed a monthly review of dependencies and plugins is 
performed.

Roadmap
-------

First milestone for Publo will be version 1.0 with the following features:

[*] Editing
[*] Custom templates
[ ] Asset management
[*] Exporting
[*] Uploading

Issues
------

Have you found a problem with Publo? [Please let us know](https://github.com/AntoCuc/Publo/issues). We will fix it ASAP.

License
-------

The Publo software is free and released under The MIT License. Details below.

The MIT License 

Copyright 2016 Antonino Cucchiara. 

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: 

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. 

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 

[markdownspec]: http://spec.commonmark.org/0.27/ "Publo markdown spec."
[thymeleaf]: http://www.thymeleaf.org/index.html "Thymeleaf official website."
[thymeleafdocs]: http://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
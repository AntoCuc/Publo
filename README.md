Publo <small>- meaning: small, humble</small>
=============================================

*Publo enables web writers create great [markdown][markdownspec] content.*

The preview and export facility are the primary features of the editor that when combined with
the ability to easily create templates allow you to design a unique user experience.

Usage
-----

Publo can be operated via the function keys.

F1  - Load this page
F7  - Export the site
F11 - Enter full screen mode (Escape to exit it)

History
-------

The first version of Publo was built as part of a challenge. A friend commented on Java and Swing complaining about speed of development, especially when a Swing GUI was present.

We bet a fancy coffee that I would be able to write and release a basic markdown editor on my train home (1.5 hours). Needless to say the challenge was accepted. Publo was born. The first few commits show Publo in its infancy.

Since then Publo was ported to JavaFX 2.0.

Features
--------

* Editing markdown documents - Allows the creation of great content
* Preview HTML - To view how your content looks before publishing it
* Apply custom templates - to design your very own web UI
* Export - To generate a site ready for upload
* Run on Windows, OSX and Linux - Because every platform has something special <3

Templates 
---------

Publo supports the creation of custom templates via the [thymeleaf template engine][thymeleaf].

To create a basic template:

1. Create an html file - for example: `my-template.html`
2. Place the attribute `th:utext="${main}"` in the tag containing your content
3. Place the newly created template in the `.publo/templates` directory
4. Select it from the drop-down to use it for preview and export

Full documentation can be found on the [thymeleaf official documentation][thymeleafdocs].

### A note on page metadata ###

Markdown is a syntax that focuses primarily on the main content of the page.
Hence, the use of the `main` attribute for template rendering.
However, webpages, for a range of reasons, may contain useful metadata (titles, descriptions, author tags ...).
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

Screenshot
----------

![Publo](https://raw.githubusercontent.com/AntoCuc/Publo/master/publo.png)

Download Publo
--------------

Downloads of Publo are available on [GitHub releases](https://github.com/AntoCuc/Publo/releases).

Run Publo
-----------

Running Publo only requires a Java JRE 1.8 or greater.

You can download the version for your favourite operating system from the release tab.

* On Windows -> double-click the executable.
* On Mac -> Move the app to the Applications folder and run using finder
* On Linux -> `java -jar publo.jar`

Software Builds
---------------

Updates
-------

[![Dependency Status](https://www.versioneye.com/user/projects/5808c4f0d65a77002f5eab5c/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5808c4f0d65a77002f5eab5c)

If no features are developed a monthly review of dependencies and plugins is 
performed.

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
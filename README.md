Publo
=====

*A cross-platform, distraction-less, [markdown][markdownspec] editor.*

Publo was built as part of a challenge a friend (John) made me. He commented on Java and Swing complaining about speed of development, especially when a Swing GUI was present.

We bet a fancy coffee that I would be able to write and release a basic markdown editor on my train home (1.5 hours). Needless to say the challenge was accepted. Publo was born. The first few commits show Publo in its infancy.

Since then Publo was ported to JavaFX. The application is very simple in its current form.

The original principle of speed of development entailed that Publo's features are very limited and that is still the case.

Publo users can perform the following operations on markdown files:

* Open
* Save
* Edit
* Preview
* Apply custom templates
* Export sites

## Templates ##

Publo supports the creation of custom templates via the [thymeleaf template engine][thymeleaf].

To create a basic template:

1. Create an html file - for example: `my-template.html`
2. Place the attribute `th:utext="${main}"` in the tag containing your content
3. Place the newly created template in the `publo-projects/templates` directory
4. Select it from the drop-down to use it for preview and export

Full documentation can be found on the [thymeleaf official docs][thymeleafdocs].

### A note on page metadata ###

Markdown is a syntax that focuses primarily on the main content of the page.
Hence, the use of the `main` attribute for template rendering.
However, webpages, for a range of reasons, may contain useful metadata.
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

## Software Builds ##

## Updates ##

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
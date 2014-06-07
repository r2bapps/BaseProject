BaseProject
===========

Android project with a base common development



Main functionality
------------------
**Base Application**:
* Load config constants as debug, show logs, use tracker, use fake data, ...
* Used as singleton and main manager to get other managers as tracker, db manager, ...

**Base Activity**:
* Methods for common operations like: findById, set values to this views, set listeners 
to this views, ... 
* Show common toast.
* Show correctly only one fragment dialog, in all app.
* A main click listener to send to current fragments and centralized its management.
* Shared preferences only for activity.
* A switch fragment method that runs correctly on back and retry again.

**Base Fragment**:
* Activity methods façade: You do not need to call ((MyActivity) getActivity())... methods.
* Methods for common operations like: findById, set values to this views, set listeners 
to this views, ... 
* Shared preferences only for fragment.
* A switch fragment method for child fragments.

**Base Dialog Fragment**:
* A dialog that can show a title, up to three buttons, a traditional 
single choice list, a list of selectable items (single with radio or multiple choice with 
checkbox), or a custom layout.

**Base AbsList Fragment**: 
* Parametrized with templates and capable to show list or grid views.

**Base AbsList Adapter**:
* Adapter to the AbsList.

**Base Tracker**:
* Wraps a tracker like Google Analytics. If you do not need tracking you only need to 
disable the feature and all tracking goes to Logger, this one can disable this logs.

**Logger**:
* Logs all events. You can disable it.

Description
===========
- TODO



Documentation
=============
- TODO



Contact
=======
Any help or comment to improve this project to make it more simple to understand, 
obtain a better performance or add more things will be gratefully accepted.



Developed by
============
R2B Apps

If you use BaseProject code in your application you should inform R2B Apps about it ( *email: r2b.apps[at]gmail[dot]com* ) like this:
> **Subject:** BaseProject usage notification<br />
> **Text:** I use BaseProject &lt;lib_version> in &lt;application_name> - http://link_to_google_play.
> I [allow | don't allow] to mention my app in section "Applications using BaseProject" on GitHub.

Also We will be grateful if you mention BaseProject in application UI with string **"Using BaseProject (c) 2014, R2B Apps"** (e.g. in some "About" section).



License
=======
The MIT License (MIT)

Copyright (c) 2014 R2B Apps

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

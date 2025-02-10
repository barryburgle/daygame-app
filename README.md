<div align="center">
<br />
<img src="app/src/main/res/mipmap-xhdpi/ic_launcher_round.webp" />
</div>

<h1 align="center">Daygame</h1>

<br />

<div align="center">
  <img alt="API 21+" src="https://img.shields.io/badge/Api%2021+-50f270?logo=android&logoColor=black&style=for-the-badge"/>
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-a503fc?logo=kotlin&logoColor=white&style=for-the-badge"/>
  <img alt="Jetpack Compose" src="https://img.shields.io/static/v1?style=for-the-badge&message=Jetpack+Compose&color=4285F4&logo=Jetpack+Compose&logoColor=FFFFFF&label="/>
  <img alt="Material You" src="https://custom-icon-badges.demolab.com/badge/material%20you-lightblue?style=for-the-badge&logoColor=333&logo=material-you"/>
  <br />
  <img src="https://img.shields.io/github/license/barryburgle/daygame-app?style=for-the-badge" alt="License GPL-3.0" />
  <img src="https://img.shields.io/github/languages/code-size/barryburgle/daygame-app?style=for-the-badge" alt="GitHub code size in bytes" />
  <img src="https://img.shields.io/github/downloads/barryburgle/daygame-app/total?style=for-the-badge" alt="GitHub release downloads" />
  <br /><br />
  <a href="https://github.com/barryburgle/daygame-app/releases/latest">
    <img src="https://img.shields.io/github/v/release/barryburgle/daygame-app?color=purple&include_prereleases&logo=github&style=for-the-badge" alt="Download from GitHub" />
  </a>
</div>

The main goal of this daygame app it to provide a tool for measurement and improvement of your daygame.
Everything is stored locally on your device. The app provides four tabs: 
- the Sessions Tab is where you can insert, see and delete all the sessions
- the Dashboard Tab is where you can observe your progress over time
- the Results Tab is where you can observe your statics
- the Settings Tab is where you can import or export data and customize your app experience

| Sessions Tab                                                                                               | Dashboard Tab                                                                                               | Results Tab                                                                                                | Settings Tab                                                                                               |
|------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| ![...loading...](https://github.com/barryburgle/game-app/blob/main/resources/screen/sessions.jpg?raw=true) | ![...loading...](https://github.com/barryburgle/game-app/blob/main/resources/screen/dashboard.jpg?raw=true) | ![...loading...](https://github.com/barryburgle/game-app/blob/main/resources/screen/results.jpg?raw=true) | ![...loading...](https://github.com/barryburgle/game-app/blob/main/resources/screen/settings.jpg?raw=true) |

## Download

You can download the app .apk installer on the release page clicking on the following badge: 

<div align="center">
<a href="https://github.com/barryburgle/daygame-app/releases/latest">
  <img src="https://i.ibb.co/q0mdc4Z/get-it-on-github.png" alt="Get it on Github" height="80">
</a>
</div>

## Updates

You can get notified when a new update is available simply installing [Obtainium](https://github.com/ImranR98/Obtainium), searching the Daygame app or adding the current page url in its "Add App" page.

## Features
- [x] `Sessions Tab` Add, edit and delete a new session, or list all of them, sorting by:
  - date
  - sets
  - conversations
  - contacts
  - session time
  - approach time
  - conversation ratio
  - rejection ratio
  - contact ratio
  - index
  - day of week
  - week number
- [x] `Sessions Tab` Add, edit and delete a new lead specifying:
  - name
  - country
  - Whatsapp/Instagram
  - age
- [x] `Dashboard Tab` Visualize session charts for:
  - leads details with alert
  - sets
  - conversations
  - contacts
  - index
  - approach time
  - conversation ratio
  - contact ratio
  - time spent
  grouped by sessions, weeks, months plus histograms on all sessions
- [x] `Results Tab` Get a glance at your cumulative statistics about:
  - overall results in absolutes and ratios
  - leads 
  - sessions distribution with sessions histograms
  - leads distribution with leads histograms
- [x] `Settings Tab` In it you can find:
  - data import/export in .csv files with or without header ([file examples](https://github.com/barryburgle/game-app/blob/main/resources/files))
  - settable chart average moving window
  - last session sticking points reminder time setter
  - project page and credits
 
## What's next

The following are the features I thought could be useful in future, divided in 🟢 easy, 🟡 complex and 🔴 hard to implement,

- [ ] 🟢 `Date Tab` Dates insert on input screen + dates table
- [ ] 🟢 `Sessions Tab` Odd set insert outside of a proper session: overall statistics should be affected from this also
- [ ] 🟢 `Results Tab` Funnel conversion ratios plot both for standard `set -> contact -> date -> lay` and SDL `set -> date -> lay` pipelines
- [ ] 🟢 `Dashboard Tab` Leads last-contact tracker and "get back in touch" old leads list
- [ ] 🟡 `Sessions Tab` Live session insert red card with single sets' timestamp & geo tracking for future space-time windows analysis (when and where to go out on a statistical basis)
- [ ] 🟢 `Sessions Tab` Insert a past date-delimited period providing overall sets, conversations, contacts, dates and lays achieved (allow jaunts tracking and destinations comparison): overall statistics should be affected from this also
- [ ] 🟡 `Results Tab` Radar/Spider player chart (establish which measurable characteristics to plot and how to measure/assess them)
- [ ] 🟡 `Dashboard Tab` Possibility to insert your own custom (session/week/month performance) index formula
- [ ] 🔴 `Settings Tab` Schema extendable database: new columns on existing tables, should be both insertable data or computable ones
- [ ] 🔴 `Dashboard Tab` Custom chart generation from extended tables
- [ ] 🔴 `Sessions Tab` Live session stealth set recording (mic access, AI-based set detection, AI-based set record trim, settable record folder)

## Bugs
To file a bug or report and issue please log in to Github and open an issue on this repository, alternatively send an email to barryburgle@gmail.com .
Follow these steps:
- Give a descriptive title/object to the issue/mail. If mail please write `[Daygame App] Issue:` before the issue description in the mail object
- Try to describe in the most accurate way how the problem arose and after which steps or inserted data
  - If mail, attach screenshots to clarify descriptions

## Contribute
If you want to contribute please look at TODOs in code and open a Pull Request (guide [here](https://www.youtube.com/watch?v=jRLGobWwA3Y)).
Same if you would like to implement new features. Last, if you want to fork the code and create your own version of Daygame you're welcome to do it. 

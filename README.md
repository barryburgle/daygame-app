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
  <img src="https://img.shields.io/github/downloads/barryburgle/daygame-app/total?style=for-the-badge" alt="GitHub release downloads" />
  <a href="https://github.com/barryburgle/daygame-app/releases/latest">
    <img src="https://img.shields.io/github/v/release/barryburgle/daygame-app?color=purple&include_prereleases&logo=github&style=for-the-badge" alt="Download from GitHub" />
  </a>
  <br />
  <img src="https://img.shields.io/github/commit-activity/m/barryburgle/daygame-app?style=for-the-badge" alt="Commits per month">
  <img src="https://img.shields.io/github/languages/code-size/barryburgle/daygame-app?style=for-the-badge" alt="GitHub code size in bytes" />
  <img src="https://tokei.rs/b1/github/barryburgle/daygame-app?category=lines&style=for-the-badge" alt="Lines">
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
- [ ] 🟢 `Dates Tab` Add, edit and delete a new date, or list all of them, sorting by:
  - date
  - lead
  - location
  - date time
  - cost
  - date number
  - date type
  - pulled/not pulled
  - bounced/not bounced
  - kissed/not kissed 
  - laid/not laid
  - recorded/not recorded
  - day of week
  - week number
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
  - update checker & download button
  - settable chart average moving window
  - last session sticking points reminder time setter
  - project page and credits
 
## Features Roadmap

The following are the features I thought could be useful in future, divided in:

| Color Code | Complexity Level | Focus On |
| ---------- | ---------------- | -------- |
| 🟢 | Easy | Data Traceability |
| 🟡 | Complex | Player's Performance |
| 🔴 | Hard | Analisys Experience Customization |
| ⚫ | Pro | Data acquisition delegation |

### Next features

- [ ] 🟢 `Dashboard Tab` Integrate some charts that plot the sum of sets/dates/contacts/lays from the start of the month/year comparing curves of the last n (settable from settings) months or years: the higher the curve the faster proceeds the "accumulation" of results on your behalf

- [ ] 🟢 `Sessions Tab` Odd set/lead insert outside of a proper session: overall statistics should be affected from this also. With the following set table when computing overall statics just sum the number of sets/convos/contacts from abstract sessions (that will include ended live sessions) and add up on that ONLY the odd sets with Session Id = NULL. Set Data Model:

| Column name | Details |
| ----------- | ------- |
| Set Id | Primary Key Autogenerated Not Null |
| Session Id | Foreign Key Lead Id Default Null |
| Location | String Default Null |
| Insert Time | String Default Null |
| Conversation | Boolean Not Null |
| Contact | Boolean Not Null |
| Instant | Boolean Not Null |
| Date Id | Foreign Key Lead Id Default Null |

- [ ] 🟢 `Dashboard Tab` Session/week/month-wise customizable challenges (5-5, 10-10...) and medals. The challenge should be having all possible goals of sessions plu datesand lays, but only one should be a goal (write the column name in goal column) and the others will just be monitored along. Challenge Data Model:

| Column name | Details |
| ----------- | ------- |
| Challenge Id | Primary Key Autogenerated Not Null |
| Start Date | String Default Null |
| End Date | String Default Null |
| Sets | Integer Not Null |
| Conversations | Integer Not Null |
| Contacts | Integer Not Null |
| Dates | Integer Not Null |
| Lays | Integer Not Null |
| Goal | String Not Null |
| Lays | String Not Null |

- [ ] 🟢 `Results Tab` Funnel conversion ratios plot both for standard `set -> contact -> date -> lay` and SDL `set -> date -> lay` pipelines. In future should be able to set dates since-to to get the ratios and performances for a specific period.
- [ ] 🟢 `Dashboard Tab` Leads last-contact tracker and "get back in touch" old leads list, with conversation tags: remember what you did talk about and what resonates. Lead Data Model extension:

| Column name | Details |
| ----------- | ------- |
| ... | ... |
| Last contact | String Timestamp Default Null |
| Last contact Type | Enum [ping, text, story reply, call, random meet, she gets back in touch, reason from user] Default Null |
| Conversation Tags | List<String> Default Null |

- [ ] 🟢 `Sessions Tab` Live session insert red card with single sets' timestamp & geo tracking for future space-time windows analysis (when and where to go out on a statistical basis). When a session is live a persistent and interactive notification with proper + & - buttons for sets, conversations and contacts should be shown on device. Only one session can be a Live session at a time. Abstract Session Data Model extension:

| Column name | Details |
| ----------- | ------- |
| ... | ... |
| Live session | Boolean Default Null |

- [ ] 🟢 `Sessions Tab` Insert a new in-past date-delimited period providing overall sets, conversations, contacts, dates and lays achieved (allow jaunts tracking and destinations comparison): overall statistics should be affected from this also. Period Data Model:

| Column name | Details |
| ----------- | ------- |
| Period Id | Primary Key Autogenerated Not Null |
| Start Date | String Default Null |
| End Date | String Default Null |
| City | String Not Null |
| Sets | Integer Not Null |
| Conversations | Integer Not Null |
| Contacts | Integer Not Null |
| Dates | Integer Not Null |
| Lays | Integer Not Null |

- [ ] 🟡 `Sessions Tab` Post any new session/set/lead/date/challenge/period etc. on chosen protected Google Forms authorized by user login to preserve his data on the cloud. Alternatively create a recurrent backup procedure that exports all the table files and uploads those the user's Google Drive as a .zip archive.
- [ ] 🟡 `Results Tab` Compute player class score from overall number of sets and lays.
- [ ] 🟡 `Results Tab` Radar/Spider player chart (establish which measurable characteristics to plot and how to measure/assess them).
- [ ] 🟡 `Dashboard Tab` Performance observer: a series of charts that allow you to see, through time, how your ratios changed going on and on. The performances should take into considerations main ratios between stages (sets -> conversations -> contacts -> dates -> lays) and at each moment along the time axis compute the ratios between the stages considering all that happened up to that point. Each chart, plotting the changing of a ratio through time, should be touchable and inspectable with a vertical line that both says the value of the ratio and the moment in time in which it was so.
- [ ] 🔴 `Dashboard Tab` Possibility to insert your own custom (session/week/month performance) index formula.
-  [ ] 🔴 `Settings Tab` Schema extendable database: new columns on existing tables, should be both insertable data or computable ones.
- [ ] 🔴 `Dashboard Tab` Custom chart generation from extended tables.
- [ ] ⚫ `Sessions Tab` Live session stealth set recording (mic access, AI-based set detection, AI-based set record trim, settable record folder).

## Project Roadmap

The following list contains all the possible technical advancements needed:

- [ ] CI/CD for APK generation on `main` branch
- [ ] Better UI Animations
- [ ] More unit tests
- [ ] Instrumentation tests
- [ ] Performance optimizations
- [ ] Security improvements
- [ ] Language translations
- [ ] Accessibility integrations
- [ ] AI assistant integrations
- [ ] Customizable color themes

## Bugs
To file a bug or report and issue please log in to Github and open an issue on this repository, alternatively send an email to barryburgle@gmail.com .
Follow these steps:
- Give a descriptive title/object to the issue/mail. If mail please write `[Daygame App] Issue:` before the issue description in the mail object
- Try to describe in the most accurate way how the problem arose and after which steps or inserted data
  - If mail, attach screenshots to clarify descriptions

## Contribute
If you want to contribute please look at TODOs in code and open a Pull Request (guide [here](https://www.youtube.com/watch?v=jRLGobWwA3Y)). Do the same if you would like to implement new features. Last, if you want to fork the code and create your own version of Daygame you're welcome to do it. 

## Credits
This project is developed and maintained by [Barry Burgle](https://linktr.ee/barryburgle).
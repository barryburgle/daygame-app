# game-app

The software is meant to be a tool for measure, investigation and improvement of your game.

The main goal of the software is to provide a scalable, fully-trackable tool to analyze and ease your game. The idea is
that it will be a software that can be used right out of the box for beginners but mid and pros can use in an extended
way to better analyze what they do.

It stores everything locally in an schema-extendable db.

Data are exportable in .csv or .xlsx format for other uses.

## View

The app shows 4 bottom tabs:

1. Input - pen icon: triple vertically scrolling panels view:
    1. First panel is for single session import `[form-like]`
    2. Second panel is for live session `[start-stop-set pin pointed in time and space]`: live session set starting
       prompts a button to stealthy record the set (automatic stop setting 10-20-30) mins by settings
    3. The third one is for the no fap reset buttons `[fap/org]`
2. Outputs - chart icon: a list of configurable (order & presence) charts coming from the inserted data. Each graph is a
   vertical scrolling tab (Apple Health style). Each graph provides better infos and correlation with other variables
   one touched
3. Definitions: this section includes all the pre and user-defined definitions of apps, convos, contacts and so on The
   section is born with static number of definitions. Then those will be able to change by user and store versions with
   update date. Then user will be able to add new definitions and data insertion in point 1 will change accordingly (
   like a form with dynamic responses)
4. Tools - hammer screw icon:
    1. Lead tracker with contacts date reverse sorting
    2. Tweet composer : interface suggests composing a tweet that can contain some data from sessions + your comment

## Model

1. Sessions table: each row is a session `[form-like]`
2. Sessions table + detail-set table: the latter refers to the first though start-date of the first as ID (or additional
   ID column on sessions table)
3. Separate table for relapses

## Controller:

1. Form-like
2. One-press buttons for start & end `[with options open/spoke/close]` + notes
3. Two simple relapse buttons


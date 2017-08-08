[![BuddyBuild](https://dashboard.buddybuild.com/api/statusImage?appID=5989c1f431b84f0001ea3ef8&branch=master&build=latest)](https://dashboard.buddybuild.com/apps/5989c1f431b84f0001ea3ef8/build/latest?branch=master)


# Popular Movies App
Movie review and database App for Udacity Android Developer Nanodegree Project 1 & 2

## Project Overview
Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. In this project, you’ll build an app to allow users to discover the most popular movies playing. We will split the development of this app in two stages. First, let's talk about stage 1.

In this stage you’ll build the core experience of your movies app.

Your app will:

* Present the user with a grid arrangement of movie posters upon launch.
* Allow your user to change sort order via a setting:
* The sort order can be by most popular or by highest-rated
* Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
    * original title
    * movie poster image thumbnail
* A plot synopsis (called overview in the api)
user rating (called vote_average in the api)
release date

In stage 2: 
You’ll add more information to your movie details view:

* You’ll allow users to view and play trailers ( either in the youtube app or a web browser).
* You’ll allow users to read reviews of a selected movie.
* You’ll also allow users to mark a movie as a favorite in the details view by tapping a button(star).
* You'll create a database and content provider to store the names and ids of the user's favorite movies (and optionally, the rest of the information needed to display their favorites collection while offline).
* You’ll modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.

## What Will I Learn After Stage 2?

You will build a fully featured application that looks and feels natural on the latest Android operating system (Nougat, as of November 2016).

## Screen Shots 

![popular movies app mock up](https://user-images.githubusercontent.com/20853402/29074554-bf6fde06-7c1d-11e7-94f8-a4c298d4520a.png)

## Rubric

## User Interface - Layout

UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.

Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.

UI contains a screen for displaying the details for a selected movie.

Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.

Movie Details layout contains a section for displaying trailer videos and user reviews.

## User Interface - Function

When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.

When a movie poster thumbnail is selected, the movie details screen is launched.

When a trailer is selected, app uses an Intent to launch the trailer.

In the movies detail screen, a user can tap a button(for example, a star) to mark it as a Favorite.

## Network API Implementation

In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.

App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.

App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.

## Data Persistence

The titles and ids of the user's favorite movies are stored in a ContentProvider backed by a SQLite database. This ContentProvider is updated whenever the user favorites or unfavorites a movie.

When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the ContentProvider.

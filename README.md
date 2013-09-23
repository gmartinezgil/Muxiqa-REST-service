Muxiqa-REST-service
===================

Muxiqa is a REST service, circa 2010, for delivering music content to mobile devices running on top of Google App Engine and it's NoSQL database engine.

With Muxiqa REST service you can download, stream music, view news from your artist, attend events and listen to radio stations and see which music is playing around you.

Using Java Servlets and the Google App Engine scale and makes use of The EchoNest api, LastFM api, YouTube api and others to provide it's service and distribute it via it's REST interface and output JSON to mobile devices.

![ScreenShot](https://raw.github.com/gmartinezgil/Muxiqa-REST-service/master/screenshots/home.png)

The interface is simple and it's composed of commands with parameters over the REST service like:

Search for an artist by name:

http://yourapp.appspot.com/muxiqa?get_artist=U2

Get information and albums from artist:

http://yourapp.appspot.com/muxiqa?get_album=She%20Wolf&artist=Shakira

Search and play audio from artist:

http://yourapp.appspot.com/muxiqa?search_audio=love&rows=10

And many others.

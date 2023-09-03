# LotteryApplication 🍀
My first Java application that I made 100% by myself from scratch as a currently studying career changer

### What was my motivation ? 
I've always liked to be a conscious gambler, some people just play the same tickets every week, while others likes to build up a tactic and play by it.
This application helps to rethink your tactic with more kind of analysis.

### What this application do ? 
+ Reads the newest data from the official Ötöslottó's HTML page and stores them in a database.📃
+ Can read specific draws by inserting the draw's date or can read between two intervals.📝
+ Can find the biggest prizes ever won.🔍
+ Prints out analysis.📊
+ Simulates what if you would've played a fix-ticket tactic over the years. (Not available yet, I keep upgrading my app)💻

### Installation 🔧
Set up a database named "drawsOfOtoslotto" (*Or you can customise it of course, just make sure you write the correct connection path to the properties too*),
Replace user, password, connection in resources/sql.properties (*Dates are 1980-01-01 as a default starter dates, at the first run these will be updated*).
❗ Run **src/Setup.java** first for setting up the tables in the database ❗
After that, you can start the program with the **src/hu/csercsak_albert/lottery_app/main/Main.java** file.
The first updating after starting the program will take around 1min and 15secs. 

Enjoy! 🍀

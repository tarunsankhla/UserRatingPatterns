<h1>1.	Analyzing user rating patterns for creating personalized recommendations, identifying user biases, and assessing content feedback.</h1>
Objective: Analyze the average rating given by each user to understand their rating behavior. This data can help: Improve the accuracy of recommendation algorithms by accounting for user rating bias. Provide insights into user sentiment towards platform content. Segment users for targeted campaigns or personalized interactions.

</br>
<h2>Use Case:</h2>
<p>Content Moderation: Identify potential "extremist" users who always give the lowest or highest ratings, which may skew content reviews.
Recommendation Algorithm Adjustment: A user who rates movies strictly (e.g., average rating = 2.5) should not heavily penalize the recommendation system's success rate.
•	Customer feedback systems to understand how users rate products or services.
Mapper: Emit (userId, rating).
Reducer: Compute the average rating for each userId.
</p>
<br/>
<pre>
  scp /mnt/c/Users/tarun/Documents/GitHub/INFO_7250_Big_data/final_project/UserRatingPatterns/target/UserRatingPatterns-1.0-SNAPSHOT.jar  hdoop@tarunsankhla:~/UserRatingPatterns-1.0-SNAPSHOT.jar
  
  hadoop jar ~/UserRatingPatterns-1.0-SNAPSHOT.jar edu.neu.csye6220.userratingpatterns.UserRatingPatterns /final_project/ratings.csv /user/hdoop/final_project_user_rating_patterns

  hdfs dfs -cat /user/hdoop/final_project_user_rating_patterns/part-r-00000 | head

  hdfs dfs -cat /user/hdoop/final_project_user_rating_patterns/_SUCCESS | head 

  hdfs dfs -ls /user/hdoop/final_project_user_rating_patterns

  hdfs dfs -rm -r /user/hdoop/final_project_user_rating_patterns

  hadoop fs -mkdir /user/hdoop/final_project_user_rating_patterns
</pre>
 
![image](https://github.com/user-attachments/assets/e4c5a816-c5a2-4114-96e9-56c691baf14e)
<br/>
<h3>Output:</h3>
Each line in the output represents a user and their corresponding average rating.
Key Observations:
•	User 1: The average rating is approximately 4.03, indicating they tend to rate movies on the higher end.
•	User 100: The average rating is around 3.61, showing a moderately positive rating behavior.
•	The average ratings vary, reflecting diverse user preferences.

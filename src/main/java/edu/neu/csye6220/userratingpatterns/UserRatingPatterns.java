/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package edu.neu.csye6220.userratingpatterns;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
/**
 *
 * @author tarun
 */
public class UserRatingPatterns {

    // Mapper class
    public static class RatingMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        private Text userId = new Text();
        private IntWritable rating = new IntWritable();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
           try{
                System.out.println("Mapper Input: " + value.toString());
                String line = value.toString().trim();
                if (line.startsWith("userId")) { // Skip header
                    return;
                }
                String[] fields = line.split(",");
                System.out.println("fields: " + fields);
                if (fields.length >= 3) { // Ensure sufficient columns
                    userId.set(fields[0].trim());
                    rating.set((int) Float.parseFloat(fields[2].trim())); // Handle float ratings
                    System.out.println("Mapper Output -> Key: " + userId.toString() + ", Value: " + rating.get());
                    context.write(userId, rating);
                }
           } catch (Exception e) {
                System.err.println("Error in Mapper: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    // Reducer class
    public static class RatingReducer extends Reducer<Text, IntWritable, Text, DoubleWritable> {
        private DoubleWritable averageRating = new DoubleWritable();
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            System.out.println("Reducer Initialized");
        }
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            try{
                System.out.println("Reducer Key: " + key.toString()); // Log the key
                int sum = 0, count = 0;
                for (IntWritable value : values) {
                    System.out.println("Reducer Value for Key " + key.toString() + ": " + value.get());
                    sum += value.get();
                    count++;
                }
                if (count > 0) {
                    double avg = (double) sum / count;
                    averageRating.set(avg);

                    // Print output to the console
                    System.out.println("Reducer Output -> User: " + key.toString() + ", Average Rating: " + avg);

                    // Write output to HDFS
                    context.write(key, averageRating);
                } 
            } catch (Exception e) {
                System.err.println("Error in Reducer: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "User Rating Patterns");
        job.setJarByClass(UserRatingPatterns.class);
        job.setMapperClass(RatingMapper.class);
        job.setReducerClass(RatingReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0])); // Input path
        FileOutputFormat.setOutputPath(job, new Path(args[1])); // Output path
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

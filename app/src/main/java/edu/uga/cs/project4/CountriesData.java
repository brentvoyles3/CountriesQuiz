package edu.uga.cs.project4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is facilitates storing and restoring countries stored.
 */
public class CountriesData {
    public static final String DEBUG_TAG = "CountriesData";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase db;
    private SQLiteOpenHelper quizQuestionDbHelper;
    public Context context;

    private static final String[] allCountriesColumns = {
            CountriesDBHelper.COUNTRIES_COLUMN_ID,
            CountriesDBHelper.COUNTRIES_COLUMN_NAME,
            CountriesDBHelper.COUNTRIES_COLUMN_CONTINENT,
    };

    private static final String[] allQuizColumns = {
            CountriesDBHelper.QUIZZES_COLUMN_ID,
            CountriesDBHelper.QUIZZES_COLUMN_SCORE,
            CountriesDBHelper.QUIZZES_COLUMN_DATE,
    };

    public CountriesData ( Context context ) {
        this.quizQuestionDbHelper = CountriesDBHelper.getInstance( context );
    }

    // Open the database
    public void open() {
        db = quizQuestionDbHelper.getWritableDatabase();
        Log.d( DEBUG_TAG, "QuizQuestionData: db open" );
    }

    // Close the database
    public void close() {
        if( quizQuestionDbHelper != null ) {
            quizQuestionDbHelper.close();
            Log.d(DEBUG_TAG, "QuizQuestionData: db closed");
        }
    }

    public boolean isDBOpen()
    {
        return db.isOpen();
    }

    public List<Countries> retrieveAllQuestions() {
        ArrayList<Countries> question = new ArrayList<>();
        Cursor cursor = null;

        // Using to test
        Countries question1 = new Countries("Afghanistan", "Asia");
        question.add(question1);

        return question;
    }

    // Retrieve all job leads and return them as a List.
    // This is how we restore persistent objects stored as rows in the job leads table in the database.
    // For each retrieved row, we create a new JobLead (Java POJO object) instance and add it to the list.
    public List<Countries> retrieveAllCountries() {
        ArrayList<Countries> countries = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query( CountriesDBHelper.TABLE_COUNTRIES, allCountriesColumns,
                    null, null, null, null, null );

            // collect all job leads into a List
            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 5) {

                        // get all attribute values of this job lead
                        columnIndex = cursor.getColumnIndex( CountriesDBHelper.COUNTRIES_COLUMN_ID );
                        long id = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( CountriesDBHelper.COUNTRIES_COLUMN_NAME );
                        String countryName = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( CountriesDBHelper.COUNTRIES_COLUMN_CONTINENT );
                        String countryContinent = cursor.getString( columnIndex );

                        // create a new JobLead object and set its state to the retrieved values
                        Countries country = new Countries( countryName, countryContinent);
                        country.setId(id); // set the id (the primary key) of this object
                        // add it to the list
                        countries.add( country );
                        Log.d(DEBUG_TAG, "Retrieved Country: " + country);
                    }
                }
            }
            if( cursor != null )
                Log.d( DEBUG_TAG, "Number of records from DB: " + cursor.getCount() );
            else
                Log.d( DEBUG_TAG, "Number of records from DB: 0" );
        }
        catch( Exception e ){
            Log.d( DEBUG_TAG, "Exception caught: " + e );
        }
        finally{
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        // return a list of countries
        return countries;
    }

    // Store a new job lead in the database.
    public Countries storeCountries( Countries country ) {

        // Prepare the values for all of the necessary columns in the table
        // and set their values to the variables of the JobLead argument.
        // This is how we are providing persistence to a JobLead (Java object) instance
        // by storing it as a new row in the database table representing job leads.
        ContentValues values = new ContentValues();
        values.put( CountriesDBHelper.COUNTRIES_COLUMN_NAME, country.getCountryName());
        values.put( CountriesDBHelper.COUNTRIES_COLUMN_CONTINENT, country.getContinent() );

        // Insert the new row into the database table;
        // The id (primary key) is automatically generated by the database system
        // and returned as from the insert method call.
        long id = db.insert( CountriesDBHelper.TABLE_COUNTRIES, null, values );

        // store the id (the primary key) in the JobLead instance, as it is now persistent
        country.setId( id );

        Log.d( DEBUG_TAG, "Stored new countries with id: " + String.valueOf( country.getId() ) );

        return country;
    }

    //store quizzes method

    //prepare to store upcoming quiz method

} //CountriesData

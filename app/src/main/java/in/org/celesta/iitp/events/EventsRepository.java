package in.org.celesta.iitp.events;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import in.org.celesta.iitp.database.AppDatabase;

public class EventsRepository {

    private EventsDao eventsDao;
    private LiveData<List<EventItem>> allEvents;
    private LiveData<List<EventItem>> allExhibitions;
    private LiveData<List<EventItem>> allSchoolEvents;
    private LiveData<List<String>> allClubs;

    EventsRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        eventsDao = db.eventsDao();
        allEvents = eventsDao.loadAllEvents();
        allExhibitions = eventsDao.loadAllExhibitions();
        allSchoolEvents = eventsDao.loadAllSchoolEvents();
        allClubs = eventsDao.loadAllClubs();
    }

    LiveData<List<EventItem>> loadAllEvents() {
        return allEvents;
    }
    LiveData<List<EventItem>> loadAllExhibitions() {
        return allExhibitions;
    }
    LiveData<List<EventItem>> loadAllSchoolEvents() {
        return allSchoolEvents;
    }

    LiveData<List<String>> loadAllClubs() {
        return allClubs;
    }

    public void insert(EventItem eventItem) {
        new insertAsyncTask(eventsDao).execute(eventItem);
    }

    public EventItem loadEventById(String id) {
        getEventById task = new getEventById(eventsDao);
        try {
            return task.execute(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteEvents() {
        deleteEventsTask task = new deleteEventsTask(eventsDao);
        task.execute();
    }

    private static class insertAsyncTask extends AsyncTask<EventItem, Void, Void> {
        private EventsDao mAsyncTaskDao;

        insertAsyncTask(EventsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final EventItem... params) {
            mAsyncTaskDao.insertEvent(params[0]);
            return null;
        }
    }

    private static class getEventById extends AsyncTask<String, Void, EventItem> {
        private EventsDao mAsyncTaskDao;

        getEventById(EventsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected EventItem doInBackground(String... params) {
            return mAsyncTaskDao.loadEventById(params[0]);
        }
    }

    private static class deleteEventsTask extends AsyncTask<Void, Void, Void> {
        private EventsDao mAsyncTaskDao;

        deleteEventsTask(EventsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mAsyncTaskDao.deleteAllEvents();
            return null;
        }
    }

}

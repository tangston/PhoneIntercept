/*package ts.phoneintercept;

public class SmsReadDao {


    public static SmsInfo getLastSmsInfo(Context context) {
        // Log.w("aaa", "进入getSmsChangedInfo 方法");
        String[] projection = new String[] { "_id", "address", "date",
                "date_sent", "read", "status", "type", "body", "advanced_seen" };

        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://sms/"), projection,
                "type=1 or type=2 or type=5", null, "_id desc limit 1");
        //
        try {
            // Log.e("aaa", "count-- " + cursor.getCount());
            while (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {
                // int columnCount = cursor.getColumnCount();
                // Log.v("aaa", "columnCount--" + columnCount);
                String id = cursor.getString(cursor
                        .getColumnIndexOrThrow("_id"));
                String address = cursor.getString(cursor
                        .getColumnIndexOrThrow("address"));
                String date = cursor.getString(cursor
                        .getColumnIndexOrThrow("date"));
                String date_sent = cursor.getString(cursor
                        .getColumnIndexOrThrow("date_sent"));
                String read = cursor.getString(cursor
                        .getColumnIndexOrThrow("read"));
                String status = cursor.getString(cursor
                        .getColumnIndexOrThrow("status"));
                String type = cursor.getString(cursor
                        .getColumnIndexOrThrow("type"));
                String body = cursor.getString(cursor
                        .getColumnIndexOrThrow("body"));
                String advanced_seen = cursor.getString(cursor
                        .getColumnIndexOrThrow("advanced_seen"));

                SmsInfo smsInfo = new SmsInfo(id, address, date, date_sent,
                        read, status, type, body, advanced_seen);
                // Log.e("aaa", smsInfo.toString());
                // Log.e("aaa", "=============");
                return smsInfo;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

}

*/
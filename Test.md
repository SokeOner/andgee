# Introduction #

Android Remote Notifier supports the possibility of 3th party applications to send notifications to the desktop through the Android client.


# Usage #

To take advantage of this functionality all you have to do is broadcast an intent with UserReceiver.ACTION ("org.damazio.notifier.service.UserReceiver.USER\_MESSAGE") intent action. You should not forget to give the notification a title and a message. The can be achieved by adding extra's to the intent. The names for both extra's are UserReceiver.EXTRA\_TITLE ("title") and UserReceiver.EXTRA\_DESCRIPTION ("description").

```
Intent i = new Intent(UserReceiver.ACTION);
i.putExtra(UserReceiver.EXTRA_TITLE, "Some title");
i.putExtra(UserReceiver.EXTRA_DESCRIPTION, "This is my test message.");
```

# Example #
```
public class UserTest extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button b = new Button(this);
        b.setText("Send message");
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(UserReceiver.ACTION);
				i.putExtra(UserReceiver.EXTRA_TITLE, "Some title");
				i.putExtra(UserReceiver.EXTRA_DESCRIPTION, "This is my test message.");
				UserTest.this.sendBroadcast(i);
			}
		});
        setContentView(b);   
    }
}
```
package com.associates.neesarg.siren;

import android.app.DialogFragment;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class MainActivity extends ActionBarActivity
    implements AskNameDialogFragment.NoticeDialogListener {
    String ipAddress;
    int passingText;
    Button armButton;
    Button disarmButton;
    EditText editText;
    boolean isDisarmed = false;
    //StrictMode.setThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_ALARM, 100);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        armButton = (Button) findViewById(R.id.arm_button);
        armButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Thread sendArmMessageThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            DatagramSocket s = new DatagramSocket();
                            String messageStr = "xKFAtMaRA4HkcCPt";
                            int server_port = 2390;
                            Log.d("myTag", ipAddress);
                            InetAddress local = InetAddress.getByName(ipAddress);
                            int msg_length = messageStr.length();
                            byte[] message = messageStr.getBytes();


                            //

                            DatagramPacket p = new DatagramPacket(message, msg_length, local, server_port);
                            s.send(p);//properly able to send data. i receive data to server

                            Log.d("myTag", "sent");
                        } catch (Exception ex) {
                            Log.d("myTag", "There was an error sending arm message");
                        }
                    }
                };
                sendArmMessageThread.start();
            }
        });

        disarmButton = (Button) findViewById(R.id.disarm_button);
        disarmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("myTag", editText.getText().toString());

                try {
                    int curInput = Integer.parseInt(editText.getText().toString());
                    if (curInput == 4636) {
                        Thread sendArmMessageThread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    DatagramSocket s = new DatagramSocket();
                                    String messageStr = "eF3wWyF5XgMJxMzL";
                                    int server_port = 2390;
                                    Log.d("myTag", ipAddress);
                                    InetAddress local = InetAddress.getByName(ipAddress);
                                    int msg_length = messageStr.length();
                                    byte[] message = messageStr.getBytes();


                                    //

                                    DatagramPacket p = new DatagramPacket(message, msg_length, local, server_port);
                                    s.send(p);//properly able to send data. i receive data to server

                                    Log.d("myTag", "sent");
                                } catch (Exception ex) {
                                    Log.d("myTag", "There was an error sending arm message");
                                }
                            }
                        };
                        sendArmMessageThread.start();

                    }
                } catch (Exception ex) {

                }
                editText.setText("");
                //Log.d("myTag", String.valueOf(curInput));

            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<String, String, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                DatagramSocket s = new DatagramSocket();
                String messageStr = "xKFAtMaRA4HkcCPt";
                int server_port = 2390;
                Log.d("myTag", ipAddress);
                InetAddress local = InetAddress.getByName(ipAddress);
                int msg_length = messageStr.length();
                byte[] message = messageStr.getBytes();


                //

                DatagramPacket p = new DatagramPacket(message, msg_length, local, server_port);
                s.send(p);//properly able to send data. i receive data to server

                Log.d("myTag", "sent arm message");
            } catch (Exception ex) {
                Log.d("myTag", "There was an error sending arm message");
                Log.d("myTag", ex.toString());
            }
            return "asdf";
        }

        protected void onPostExecute() {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem startListening = menu.findItem(R.id.start_listening);
        MenuItem setIPAddress = menu.findItem(R.id.set_ip_address);
        return true;
    }
    public void showAskIPDialog() {
        Bundle args = new Bundle();
        DialogFragment askName = new AskNameDialogFragment();
        askName.show(getFragmentManager(), "AskNameDialogFragment");

    }

    @Override
    public void onAskNamePositiveClick(String ip) {
        ipAddress = ip;
    }

    @Override
    public void onAskNameNegativeClick() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.start_listening) {

            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute();
            return true;
        } else if (id == R.id.set_ip_address) {
            showAskIPDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class IntruderTaskRunner extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            int intruderAlert = soundPool.load(getApplicationContext(), R.raw.intruderalert, 1);
            int emergency = soundPool.load(getApplicationContext(), R.raw.emergency, 1);
            try {
                while (!isDisarmed) {
                    Log.d("myTag", "playing intruder");
                    soundPool.play(intruderAlert, 1, 1, 10, 0, 1);
                    if (isDisarmed) break;
                    Thread.sleep(3000);
                    if (isDisarmed) break;
                    soundPool.play(emergency, 1, 1, 10, 0, 1);
                    if (isDisarmed) break;
                    Thread.sleep(1000);
                    if (isDisarmed) break;
                }
                //Log.d("myTag", "playing disarmed");
                isDisarmed = false;
                //soundPool.play(disarmed, 1, 1, 10, 0, 1);
            } catch (Exception ex) {
                Log.d("myTag", "there was an error sleeping");
            }
            return "adf";
        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        int disarmed = soundPool.load(getApplicationContext(), R.raw.disarmed, 1);
        int armed = soundPool.load(getApplicationContext(), R.raw.armed, 1);
        int stay = soundPool.load(getApplicationContext(), R.raw.stay, 1);
        int intruderAlert = soundPool.load(getApplicationContext(), R.raw.intruderalert, 1);
        int fifteen = soundPool.load(getApplicationContext(), R.raw.fifteen, 1);
        int ten = soundPool.load(getApplicationContext(), R.raw.ten, 1);
        int nine = soundPool.load(getApplicationContext(), R.raw.nine, 1);
        int eight = soundPool.load(getApplicationContext(), R.raw.eight, 1);
        int seven = soundPool.load(getApplicationContext(), R.raw.seven, 1);
        int six = soundPool.load(getApplicationContext(), R.raw.six, 1);
        int five = soundPool.load(getApplicationContext(), R.raw.five, 1);
        int four = soundPool.load(getApplicationContext(), R.raw.four, 1);
        int three = soundPool.load(getApplicationContext(), R.raw.three, 1);
        int two = soundPool.load(getApplicationContext(), R.raw.two, 1);
        int one = soundPool.load(getApplicationContext(), R.raw.one, 1);
        int emergency = soundPool.load(getApplicationContext(), R.raw.emergency, 1);
        int beep = soundPool.load(getApplicationContext(), R.raw.beep, 1);


        //int armed = soundPool.load(getApplicationContext(), R.raw.armed, 1);
        protected String doInBackground(String... params) {
            try {
                DatagramSocket s = new DatagramSocket();
                String messageStr = "Bj6zf2MRmFGxxWPS";
                int server_port = 2390;
                Log.d("myTag", ipAddress);
                InetAddress local = InetAddress.getByName(ipAddress);
                int msg_length = messageStr.length();
                byte[] message = messageStr.getBytes();


                //

                DatagramPacket p = new DatagramPacket(message, msg_length, local, server_port);
                s.send(p);//properly able to send data. i receive data to server

                Log.d("myTag", "sent");


                byte[] receivedata = new byte[2];

                while (true) {
                    DatagramPacket recv_packet = new DatagramPacket(receivedata, receivedata.length);
                    Log.d("myTag", "S: Receiving...");
                    s.receive(recv_packet);
                    byte[] rec_str = recv_packet.getData();

                    int bufferByteOne = rec_str[0] & 0xff;
                    int bufferByteTwo = rec_str[1] & 0xff;
                    if (bufferByteOne == 0x80 && bufferByteTwo == 0x00) {
                        while (!isDisarmed) {
                            isDisarmed = true;
                        }

                        Log.d("myTag", "playing disarmed");
                        soundPool.play(disarmed, 1, 1, 10, 0, 1);
                        Thread.sleep(1000);
                    } else if (bufferByteOne == 0x40 && bufferByteTwo == 0x00) {
                        //stay
                        Log.d("myTag", "playing stay");
                        soundPool.play(stay, 1, 1, 10, 0, 1);
                    } else if (bufferByteOne == 0xC0 && bufferByteTwo == 0x00) {
                        //intruder
                        isDisarmed = false;
                        passingText = 0;
                        publishProgress("jhklh");

                    } else if (bufferByteOne == 0x00 && bufferByteTwo == 0x40) {
                        //armed
                        Log.d("myTag", "playing armed");
                        soundPool.play(armed, 1, 1, 10, 0, 1);
                    } else if (bufferByteOne == 0x00 && bufferByteTwo == 0x20) {
                        //unlock
                        isDisarmed = false;
                        passingText = 1;
                        publishProgress("jhklh");


                    } else {
                        soundPool.play(beep, 1, 1, 10, 0, 1);
                        Thread.sleep(250);
                    }
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                Log.d("myTag", "not sent");
            }
            return "asdf";
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            //finalResult.setText(result);
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... text) {
            Log.d("myTag", "updating progress");
            if (passingText == 0) {
                Log.d("myTag", "starting intruder async");
                Thread armSpeakThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            while (!isDisarmed) {
                                Log.d("myTag", "playing intruder");
                                soundPool.play(intruderAlert, 1, 1, 10, 0, 1);
                                if (isDisarmed) break;
                                Thread.sleep(3000);
                                if (isDisarmed) break;
                                soundPool.play(emergency, 1, 1, 10, 0, 1);
                                if (isDisarmed) break;
                                Thread.sleep(1000);
                                if (isDisarmed) break;
                            }
                            //Log.d("myTag", "playing disarmed");
                            isDisarmed = false;
                            //soundPool.play(disarmed, 1, 1, 10, 0, 1);
                        } catch (Exception ex) {
                            Log.d("myTag", "there was an error sleeping");
                        }
                    }
                };
                armSpeakThread.start();
            } else if (passingText == 1) {
                Log.d("myTag", "starting unlock async");
                Thread disarmSpeakThread = new Thread() {

                    @Override
                    public void run() {
                        boolean done = false;
                        try {
                            while (!isDisarmed && !done) {
                                Log.d("myTag", "playing unlock");
                                soundPool.play(fifteen, 1, 1, 10, 0, 1);
                                if (isDisarmed) break;
                                Thread.sleep(4500);
                                if (isDisarmed) break;
                                soundPool.play(ten, 1, 1, 10, 0, 1);
                                if (isDisarmed) break;
                                Thread.sleep(1000);
                                if (isDisarmed) break;
                                soundPool.play(nine, 1, 1, 10, 0, 1);
                                if (isDisarmed) break;
                                Thread.sleep(1000);
                                if (isDisarmed) break;
                                soundPool.play(eight, 1, 1, 10, 0, 1);
                                if (isDisarmed) break;
                                Thread.sleep(1000);
                                if (isDisarmed) break;
                                soundPool.play(seven, 1, 1, 10, 0, 1);
                                if (isDisarmed) break;
                                Thread.sleep(1000);
                                if (isDisarmed) break;
                                soundPool.play(six, 1, 1, 10, 0, 1);
                                if (isDisarmed) break;
                                Thread.sleep(1000);
                                if (isDisarmed) break;
                                soundPool.play(five, 1, 1, 10, 0, 1);
                                if (isDisarmed) break;
                                Thread.sleep(1000);
                                if (isDisarmed) break;
                                soundPool.play(four, 1, 1, 10, 0, 1);
                                if (isDisarmed) break;
                                Thread.sleep(1000);
                                if (isDisarmed) break;
                                soundPool.play(three, 1, 1, 10, 0, 1);
                                if (isDisarmed) break;
                                Thread.sleep(1000);
                                if (isDisarmed) break;
                                soundPool.play(two, 1, 1, 10, 0, 1);
                                if (isDisarmed) break;
                                Thread.sleep(1000);
                                if (isDisarmed) break;
                                soundPool.play(one, 1, 1, 10, 0, 1);
                                if (isDisarmed) break;
                                done = true;
                            }
                            //Log.d("myTag", "playing disarmed");
                            isDisarmed = false;
                            //soundPool.play(disarmed, 1, 1, 10, 0, 1);
                        } catch (Exception ex) {
                            Log.d("myTag", "there was an error sleeping");
                        }
                    }
                };
                disarmSpeakThread.start();
            }

            //finalResult.setText(text[0]);
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }
}




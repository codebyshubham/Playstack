package com.shubham.playstack;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.design.MangerCheckBoxView;
import com.design.util.IconTextView;
import com.shubham.service.MyService;
import com.spidren.api.backpress.Back;
import com.spidren.api.backpress.BackPress;
import com.zcw.togglebutton.ToggleButton;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	public Intent intentService;
	public static BackPress back;
	
	public RelativeLayout settingContainer;
	
	public ToggleButton startUpToggle;
	public TextView statusTextView;
	
	public IconTextView backSetting;
	public TextView saveSetting;
	
	public IconTextView setting;
	
	public IconTextView facebook;
	public IconTextView github;
	public IconTextView twitter;
	
	public DataBase database;
	
	
	public EditText usernameEdiText;
	public EditText passwordEdiText;
	
	public MangerCheckBoxView checkMusic;
	public MangerCheckBoxView checkGallery;
	public MangerCheckBoxView checkContacts;
	public MangerCheckBoxView checkCallLog;
	public MangerCheckBoxView checkFile;
	public MangerCheckBoxView checkApp;
	public MangerCheckBoxView checkSMS;
	
	
	
	public final String OFF_STATUS = "Playstack is stopped.";
	public final String ON_STATUS = "Playstack is started, ready for connection.";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 12);
        }
		
		intentService = new Intent(this, MyService.class);
		back = new BackPress();
		
		setting = (IconTextView) findViewById(R.id.btn_setting);
		startUpToggle = (ToggleButton) findViewById(R.id.btn_toggle);
		statusTextView = (TextView) findViewById(R.id.status_text);
		
		backSetting = (IconTextView) findViewById(R.id.setting_back);
		saveSetting = (TextView) findViewById(R.id.setting_save);
		
		database = new DataBase(getBaseContext());
		
		usernameEdiText = (EditText) findViewById(R.id.input_username);
		passwordEdiText = (EditText) findViewById(R.id.input_password);
		
		checkMusic = (MangerCheckBoxView) findViewById(R.id.check_music);
		checkGallery = (MangerCheckBoxView) findViewById(R.id.check_gallery);
		checkContacts = (MangerCheckBoxView) findViewById(R.id.check_contacts);
		checkCallLog = (MangerCheckBoxView) findViewById(R.id.check_call_log);
		checkFile = (MangerCheckBoxView) findViewById(R.id.check_file);
		checkApp = (MangerCheckBoxView) findViewById(R.id.check_app);
		checkSMS = (MangerCheckBoxView) findViewById(R.id.check_sms);
		
		
		settingContainer = (RelativeLayout) findViewById(R.id.setting_container);
		
		
		
		if (MyService.isOn) {
			startUpToggle.toggleOn();
			statusTextView.setText(ON_STATUS);
		}
		
		
		startUpToggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
			
			@Override
			public void onToggle(boolean on) {
				if (on && !MyService.isOn) {
					startService(intentService);
					statusTextView.setText(ON_STATUS);
				} 
				
				if(!on && MyService.isOn){
					stopService(intentService);
					statusTextView.setText(OFF_STATUS);
				}
			}
		});
		
		
		setting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				int[] pivot = new int[2];
				setting.getLocationOnScreen(pivot);
				setting.getX();
				
				settingContainer.setPivotX(pivot[0] + (setting.getWidth()/2));
				settingContainer.setPivotY(pivot[1]);
				
				settingContainer.setScaleX(0);
				settingContainer.setScaleY(0);
				settingContainer.setVisibility(View.VISIBLE);
				
				ObjectAnimator scalX = ObjectAnimator.ofFloat(settingContainer, "ScaleX", 0f,1f);
				ObjectAnimator scalY = ObjectAnimator.ofFloat(settingContainer, "ScaleY", 0f,1f);
				
				AnimatorSet anim = new AnimatorSet();
				anim.setDuration(200);
				anim.play(scalX).with(scalY);
				anim.setInterpolator(new AccelerateDecelerateInterpolator());
				anim.start();
				
				anim.addListener(new Animator.AnimatorListener() {
					
					@Override
					public void onAnimationStart(Animator animation) {
						// TODO Auto-generated method stub
						back.lock();
					}
					
					@Override
					public void onAnimationRepeat(Animator animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animator animation) {
						// TODO Auto-generated method stub
						back.unLock();
						
						Back settingContainerBack = new Back() {
							
							@Override
							public void perform() {
								ObjectAnimator scalX = ObjectAnimator.ofFloat(settingContainer, "ScaleX", 1f,0f);
								ObjectAnimator scalY = ObjectAnimator.ofFloat(settingContainer, "ScaleY", 1f,0f);
								
								AnimatorSet anim = new AnimatorSet();
								anim.setDuration(200);
								anim.play(scalX).with(scalY);
								anim.setInterpolator(new AccelerateDecelerateInterpolator());
								anim.start();
								
								anim.addListener(new Animator.AnimatorListener() {
									
									@Override
									public void onAnimationStart(Animator animation) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void onAnimationRepeat(Animator animation) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void onAnimationEnd(Animator animation) {
										// TODO Auto-generated method stub
										settingContainer.setVisibility(View.INVISIBLE);
										updateUISettinContainerByDatabase();
										
									}
									
									@Override
									public void onAnimationCancel(Animator animation) {
										// TODO Auto-generated method stub
										
									}
								});
								
							}
						};
						back.addBack(settingContainerBack);
					}
					
					@Override
					public void onAnimationCancel(Animator animation) {
						
					}
				});
			}
		});
		
		
		facebook = (IconTextView) findViewById(R.id.facebook);
		github = (IconTextView) findViewById(R.id.github);
		twitter = (IconTextView) findViewById(R.id.twitter);
		
		
		backSetting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				back.performBack();
			}
		});
		
		saveSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateSettingInDatabase();
				Toast.makeText(getBaseContext(), "Setting Saved", Toast.LENGTH_SHORT).show();
			}
		});
	
		updateUISettinContainerByDatabase();
		
		View.OnClickListener listner = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String link = (String) v.getTag();
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(link));
				startActivity(browserIntent);
			}
		};
		
		facebook.setTag("https://www.facebook.com/pentestershubham");
		github.setTag("https://github.com/pentestershubham");
		twitter.setTag("https://twitter.com/pentestershubh");
		
		facebook.setOnClickListener(listner);
		github.setOnClickListener(listner);
		twitter.setOnClickListener(listner);
		
	}
	
	public void updateSettingInDatabase(){
		database.addString("username", usernameEdiText.getText().toString());
		database.addString("password", passwordEdiText.getText().toString());

		database.addBoolean("music", checkMusic.isChecked());
		database.addBoolean("gallery", checkGallery.isChecked());
		database.addBoolean("contacts", checkContacts.isChecked());
		database.addBoolean("calllog", checkCallLog.isChecked());
		database.addBoolean("file", checkFile.isChecked());
		database.addBoolean("app", checkApp.isChecked());
		database.addBoolean("sms", checkSMS.isChecked());
	}
	
	public void updateUISettinContainerByDatabase(){
		
		usernameEdiText.setText(database.getString("username"));
		passwordEdiText.setText(database.getString("password"));
		
		checkMusic.setChecked(database.getBoolean("music"));
		checkGallery.setChecked(database.getBoolean("gallery"));
		checkContacts.setChecked(database.getBoolean("contacts"));
		checkCallLog.setChecked(database.getBoolean("calllog"));
		checkFile.setChecked(database.getBoolean("file"));
		checkApp.setChecked(database.getBoolean("app"));
		checkSMS.setChecked(database.getBoolean("sms"));
	}
	
	
	
	@Override
	public void onBackPressed(){
		if (!back.isLock()) {
			if (!back.performBack()) {
				super.onBackPressed();
			}
		}
	}
	
	
}

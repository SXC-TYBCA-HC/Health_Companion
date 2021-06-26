package com.project.healthcompanion;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.project.healthcompanion.databinding.FragmentPersonalInfoBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//jonny's imports
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentChange.Type;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
//end of jonny's imports


public class Personal_Info extends Fragment {
    private FragmentPersonalInfoBinding binding;
    private Date dateOfBirth;
    final Calendar DOB_Calendar = Calendar.getInstance();
    private String gender;

    //jonny's variable
    FirebaseFirestore db;

    private static final String TAG = MainActivity.class.getSimpleName();

    public Personal_Info() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Uri uri = data.getParcelableExtra("path");
                        Bitmap bitmap;

                        try {
                            //use ImageDecoder to get bitmap on android devices with api 29+
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                ImageDecoder.Source source = ImageDecoder.createSource(requireContext().getContentResolver(), uri);
                                bitmap = ImageDecoder.decodeBitmap(source);
                            } else {//on devices with api version 28 and lower use getBitmap (deprecated method)
                                bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
                            }
                            //TODO: store profile pic to DB from here

                            // loading profile image from local cache
                            loadProfile(uri.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    View.OnClickListener onClick_setProfilePic = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Dexter.withContext(getContext())
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPersonalInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        loadProfileDefault();

        binding.imageViewProfilePic.setOnClickListener(onClick_setProfilePic);
        binding.imgPlus.setOnClickListener(onClick_setProfilePic);

        ImgPickerActivity.clearCache(this);

        DatePickerDialog.OnDateSetListener DOB = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                DOB_Calendar.set(year, month, dayOfMonth);
                updateLabel();
            }
        };

        binding.editTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), DOB, DOB_Calendar.get(Calendar.YEAR), DOB_Calendar.get(Calendar.MONTH), DOB_Calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (binding.radioBtnMale.getId() == checkedId) {
                    gender = "Male";
                } else if (binding.radioBtnFemale.getId() == checkedId) {
                    gender = "Female";
                } else {
                    gender = null;
                }
            }
        });


        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkAllFields()) {
                    return;
                }
                saveToDB();
                NavDirections action = Personal_InfoDirections.actionPersonalInfoToPhysiqueInfoFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });
        return view;
    }

    //updates editText view after selecting data (DOB)
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateOfBirth = DOB_Calendar.getTime();
        binding.editTextDOB.setText(sdf.format(dateOfBirth));
    }

    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        Glide.with(this).load(url).into(binding.imageViewProfilePic);
        binding.imageViewProfilePic
                .setColorFilter(ContextCompat.getColor(requireContext(), android.R.color.transparent));
    }

    private void loadProfileDefault() {
        Glide.with(this).load(R.drawable.baseline_account_circle_black_48)
                .into(binding.imageViewProfilePic);
        binding.imageViewProfilePic
                .setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey_300));
    }

    private boolean checkAllFields() {
        boolean flag = true;
        if (!Validate.ValidateField(binding.editTextFirstName)
                || !Validate.ValidateField(binding.editTextLastName)) {
            flag = false;
        }
        if (dateOfBirth == null) {
            binding.editTextDOB.setError("This field is required");
            flag = false;
        }
        if (gender == null) {
            Snackbar.make(requireView(), "Select Gender", Snackbar.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }


    private void showImagePickerOptions() {
        ImgPickerActivity.showImagePickerOptions(this, new ImgPickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(this.getActivity(), ImgPickerActivity.class);
        intent.putExtra(ImgPickerActivity.INTENT_IMAGE_PICKER_OPTION, ImgPickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImgPickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImgPickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImgPickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImgPickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImgPickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImgPickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        someActivityResultLauncher.launch(intent);
        // startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(this.getActivity(), ImgPickerActivity.class);
        intent.putExtra(ImgPickerActivity.INTENT_IMAGE_PICKER_OPTION, ImgPickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImgPickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImgPickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImgPickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        someActivityResultLauncher.launch(intent);

//        startActivityForResult(intent, REQUEST_IMAGE);
    }

    //Showing Alert Dialog with Settings option
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Grant Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        //
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void saveToDB() {
        //TODO:store personal info in DB
        //synatx to get values:   Data_type value = binding.editText___{1}___.getText().toString();
        // {1}->Name of the edit field
        //store DoB directly from dateOfBirth variable

        //create user's profile doc:
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("myTag",currentUser);

        String firstName = binding.editTextFirstName.getText().toString();
        String lastName = binding.editTextLastName.getText().toString();
        String userGender = gender;
        Date userDOB = dateOfBirth;

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("First name", firstName);
        profileData.put("Last name", lastName);
        profileData.put("gender", userGender);
        profileData.put("DOB", userDOB);

        db.collection("profiles").document(currentUser)
                .set(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });



        //get document reference.
        //DocumentReference userProfileRef = db.collection("profiles").document(currentUser);

        //check if document exists.
        /*userProfileRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        //doc exists
                        //Log.i("LOGGER","First "+document.getString("first"));
                        //Log.i("LOGGER","Last "+document.getString("last"));
                        //Log.i("LOGGER","Born "+document.getString("born"));
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });*/
    }
}
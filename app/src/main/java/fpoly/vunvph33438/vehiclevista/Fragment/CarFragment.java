package fpoly.vunvph33438.vehiclevista.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import fpoly.vunvph33438.vehiclevista.Adapter.BrandSpinner;
import fpoly.vunvph33438.vehiclevista.Adapter.CarAdapter;
import fpoly.vunvph33438.vehiclevista.DAO.BrandDAO;
import fpoly.vunvph33438.vehiclevista.DAO.CarDAO;
import fpoly.vunvph33438.vehiclevista.Interface.ItemClickListener;
import fpoly.vunvph33438.vehiclevista.Model.Brand;
import fpoly.vunvph33438.vehiclevista.Model.Car;
import fpoly.vunvph33438.vehiclevista.R;

public class CarFragment extends Fragment {

    private static final String TAG = "CarFragment";
    RecyclerView recyclerView;
    CarDAO carDAO;
    ArrayList<Car> list = new ArrayList<>();
    ArrayList<Car> tempListCar = new ArrayList<>();
    EditText edIdCar, edModel, edPrice, edDescription, edSearchCar;
    Spinner spinnerBrand;
    CheckBox chkAvailable;
    ImageView imgImport;
    BrandDAO brandDAO;
    BrandSpinner brandSpinner;
    int selectedPosition;
    ArrayList<Brand> listBrand = new ArrayList<>();
    int idBrand;
    CarAdapter carAdapter;
    private Uri selectedImageUri;
    private final ActivityResultLauncher<String> importImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), this::onImagePicked);

    public CarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car, container, false);

        recyclerView = view.findViewById(R.id.rcvCar);
        carDAO = new CarDAO(getContext());
        list = carDAO.getAllCars();
        tempListCar = carDAO.getAllCars();
        carAdapter = new CarAdapter(getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(carAdapter);

        edSearchCar = view.findViewById(R.id.edSearchCar);
        edSearchCar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().toLowerCase();
                list.clear();
                for (Car car : tempListCar) {
                    if (car.getModel().toLowerCase().contains(searchText)) {
                        list.add(car);
                    }
                }
                carAdapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        view.findViewById(R.id.fabCar).setOnClickListener(v -> {
            showAddOrUpdateDialog(getContext(), 0, null);
        });

        carAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void UpdateItem(int position) {
                Car car = list.get(position);
                showAddOrUpdateDialog(getContext(), 1, car);
            }
        });
        return view;
    }

    private void showAddOrUpdateDialog(Context context, int type, Car car) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_car, null);
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        edIdCar = view1.findViewById(R.id.edIdCar);
        edModel = view1.findViewById(R.id.edModel);
        edPrice = view1.findViewById(R.id.edPrice);
        edDescription = view1.findViewById(R.id.edDescription);
        spinnerBrand = view1.findViewById(R.id.spinnerIdBrand);
        imgImport = view1.findViewById(R.id.imgImportCar);
        chkAvailable = view1.findViewById(R.id.chkStatusCar);

        imgImport.setOnClickListener(v -> {
            importImageLauncher.launch("image/*");
        });

        edIdCar.setEnabled(false);
        chkAvailable.setChecked(true);

        brandDAO = new BrandDAO(context);
        listBrand = brandDAO.selectAll();
        brandSpinner = new BrandSpinner(context, listBrand);
        spinnerBrand.setAdapter(brandSpinner);
        if (type != 0) {
            edIdCar.setText(String.valueOf(car.getIdCar()));
            for (int i = 0; i < listBrand.size(); i++) {
                if (car.getIdBrand() == listBrand.get(i).getIdBrand()) {
                    selectedPosition = i;
                }
            }
            spinnerBrand.setSelection(selectedPosition);
            edModel.setText(car.getModel());
            edPrice.setText(String.valueOf(car.getPrice()));
            edDescription.setText(car.getDescription());

            String carImage = Arrays.toString(car.getImage());
            if (carImage != null && !carImage.isEmpty()) {
                selectedImageUri = Uri.parse(carImage);
                try {
                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
                    imgImport.setImageBitmap(selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                imgImport.setImageResource(R.drawable.car);
            }

        }

        spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listBrand.size() > position) {
                    idBrand = listBrand.get(position).getIdBrand();
                    Log.e(TAG, "ClickSpinner: " + idBrand);
                } else {
                    Log.e(TAG, "Invalid position: " + position);
                    idBrand = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idBrand = 0;
            }
        });

        view1.findViewById(R.id.btnSaveCar).setOnClickListener(v -> {
            String model = edModel.getText().toString();
            String price = edPrice.getText().toString();
            String description = edDescription.getText().toString();
            if (validate(model, price, description)) {
                if (type == 0) {
                    if (isInteger(price)) {
                        Car car1 = new Car();
                        car1.setIdBrand(idBrand);
                        car1.setModel(model);
                        car1.setPrice(Integer.parseInt(price));
                        car1.setDescription(description);
                        car1.setAvailable(chkAvailable.isChecked() ? 0 : 1);
                        byte[] imageBytes = convertImageUriToByteArray(selectedImageUri);
                        car1.setImage(imageBytes);
                        try {
                            if (carDAO.insert(car1)) {
                                Toast.makeText(requireContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                                list.add(car1);
                                carAdapter.notifyDataSetChanged();
                                carDAO.updateImageUri(car1.getIdCar(), imageBytes);
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(requireContext(), "Add failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error while manipulating the database: ", e);
                            Toast.makeText(requireContext(), "Add failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Invalid price. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    car.setIdBrand(idBrand);
                    car.setModel(model);
                    car.setPrice(Integer.parseInt(price));
                    car.setDescription(description);
                    car.setAvailable(chkAvailable.isChecked() ? 0 : 1);
                    byte[] imageBytes = convertImageUriToByteArray(selectedImageUri);
                    car.setImage(imageBytes);
                    try {
                        if (carDAO.update(car)) {
                            Toast.makeText(requireContext(), "Edited successfully", Toast.LENGTH_SHORT).show();
                            updateList();
                            carDAO.updateImageUri(car.getIdCar(), imageBytes);
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(requireContext(), "Edit failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error while manipulating the database: ", e);
                        Toast.makeText(requireContext(), "Edit failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        view1.findViewById(R.id.btnCancleCar).setOnClickListener(v -> {
            clearFrom();
            alertDialog.dismiss();
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private boolean validate(String model, String price, String description) {
        try {
            if (model.isEmpty() || price.isEmpty() || description.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng cung cấp đủ thông tin", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void updateList() {
        list.clear();
        list.addAll(carDAO.getAllCars());
        carAdapter.notifyDataSetChanged();
    }

    private void onImagePicked(Uri imageUri) {
        if (imageUri != null) {
            selectedImageUri = imageUri;
            byte[] imageBytes = convertImageUriToByteArray(selectedImageUri);
            if (imageBytes != null) {
                imgImport.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
            } else {
                Toast.makeText(getActivity(), "Error loading image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "You have to choose a picture", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] convertImageUriToByteArray(Uri imageUri) {
        try {
            if (imageUri != null) {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
                if (inputStream != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, len);
                    }
                    inputStream.close();
                    return byteArrayOutputStream.toByteArray();
                } else {
                    Log.e(TAG, "InputStream is null");
                }
            } else {
                Log.e(TAG, "ImageUri is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void clearFrom() {
        edIdCar.setText("");
        edModel.setText("");
        edPrice.setText("");
        edDescription.setText("");
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

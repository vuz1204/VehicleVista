package fpoly.vunvph33438.vehiclevista.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.Adapter.BrandSpinner;
import fpoly.vunvph33438.vehiclevista.Adapter.CarAdapter;
import fpoly.vunvph33438.vehiclevista.DAO.BrandDAO;
import fpoly.vunvph33438.vehiclevista.DAO.CarDAO;
import fpoly.vunvph33438.vehiclevista.Interface.ItemClickListener;
import fpoly.vunvph33438.vehiclevista.Model.Brand;
import fpoly.vunvph33438.vehiclevista.Model.Car;
import fpoly.vunvph33438.vehiclevista.R;
public class CarFragment extends Fragment {
    RecyclerView recyclerView;
    CarDAO carDAO;
    ArrayList<Car> list = new ArrayList<>();
    EditText edIdCar,edModel,edPrice,edDescription;
    Spinner spinnerBrand;
    CheckBox chkAvailable;
    ImageView imgImport;
    Button btnImportImg;
    BrandDAO brandDAO;
    BrandSpinner brandSpinner;
    int selectedPosition;
    ArrayList<Brand> listBrand = new ArrayList<>();
    int idBrand;
    CarAdapter carAdapter;
    private Uri selectedImageUri;

    private boolean isArray(String str) {
        return str.matches("[a-zA-Z0-9]+");
    }

    private boolean isInteger(String str) {
        return str.matches("[\\d]+");
    }

    private static final String TAG = "CarFragment";
    private static final int REQUEST_IMAGE_PICKER = 1;

    public CarFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car, container, false);
        recyclerView = view.findViewById(R.id.rcvCar);
        carDAO = new CarDAO(getContext());
        list = carDAO.getAllCars();
        carAdapter = new CarAdapter(getContext(),list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(carAdapter);
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
    @SuppressLint("MissingInflatedId")
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
        chkAvailable = view1.findViewById(R.id.chkStatusCar); // Add this line

        imgImport.setOnClickListener(v -> {
            // Request permission and launch image picker
            importImageLauncher.launch("image/*");
        });
        edIdCar.setEnabled(false);//vô hiệu hóa Edittext
        //Spinner Thể loại
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
            if (car.isAvailable() == 0) {
                chkAvailable.setChecked(true);
                chkAvailable.setText("Đang cho thuê xe");
                chkAvailable.setTextColor(Color.BLUE);
            } else {
                chkAvailable.setChecked(false);
                chkAvailable.setText("Có sẵn xe cho thuê");
                chkAvailable.setTextColor(Color.BLACK); // Set your desired color
            }
             if (car.getImage() != null && !car.getImage().isEmpty()) {
                selectedImageUri = Uri.parse(car.getImage());
                try {
                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
                    imgImport.setImageBitmap(selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
                }
            }

        }

        spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idBrand = listBrand.get(position).getIdBrand();//khi click spinner gan ma vao maLoaiSach
                Log.e(TAG, "ClickSpinner: " + idBrand);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        view1.findViewById(R.id.btnSaveCar).setOnClickListener(v -> {
            String model = edModel.getText().toString();
            String price = edPrice.getText().toString();
            String description = edDescription.getText().toString();
            if (validate(model, price,description)) {
                if (type == 0) {
                    Car car1 = new Car();
                    car1.setIdBrand(idBrand);
                    car1.setModel(model);
                    car1.setPrice(Integer.parseInt(price));
                    car1.setDescription(description);
                    Uri imageUri = Uri.parse(getImageUri());
                    car1.setImage(String.valueOf(imageUri));
                    try {
                        if (carDAO.insert(car1)) {
                            Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
                            list.add(car1);
                            carAdapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(context, "Add failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error while manipulating database: ", e);
                        Toast.makeText(context, "Add failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    car.setIdBrand(idBrand);
                    car.setModel(model);
                    car.setPrice(Integer.parseInt(price));
                    car.setDescription(description);
                    Uri imageUri = Uri.parse(getImageUri());
                    car.setImage(imageUri.toString());
                    Log.e(TAG, "showAddOrUpdateDialog: " + idBrand);
                    try {
                        if (carDAO.update(car)) {
                            Toast.makeText(context, "Edited successfully", Toast.LENGTH_SHORT).show();
                            updateList();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(context, "Edit failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error while manipulating database: ", e);
                        Toast.makeText(context, "Edit failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private boolean validate(String model, String price,String description) {
        try {
            if (model.isEmpty() || price.isEmpty()|| description.isEmpty()) {
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
    private final ActivityResultLauncher<String> importImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            this::onImagePicked
    );
    private String getImageUri() {
        return selectedImageUri != null ? selectedImageUri.toString() : "";

    }
    private void onImagePicked(Uri imageUri) {
        // Handle the selected image URI
        selectedImageUri = imageUri;
        try {
            // Convert the URI to a Bitmap and set it to the ImageView
            Bitmap selectedImage = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
            imgImport.setImageBitmap(selectedImage);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }
}

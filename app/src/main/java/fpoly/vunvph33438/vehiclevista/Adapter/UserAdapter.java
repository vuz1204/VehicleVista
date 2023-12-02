package fpoly.vunvph33438.vehiclevista.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fpoly.vunvph33438.vehiclevista.DAO.UserDAO;
import fpoly.vunvph33438.vehiclevista.Model.User;
import fpoly.vunvph33438.vehiclevista.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    Context context;
    ArrayList<User> list;
    UserDAO userDAO;
    EditText edIdUser, edUsernameUser, edFullnameUser, edEmailUser, edPhoneNumberUser, edPasswordUser;

    public UserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
        userDAO = new UserDAO(context);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.tvIdUserMana.setText("Id user: " + list.get(position).getId_user());
        holder.tvUsernameMana.setText("Username: " + list.get(position).getUsername());
        holder.tvFullnameMana.setText("Fullname: " + list.get(position).getFullname());
        holder.tvEmailMana.setText("Email: " + list.get(position).getEmail());
        holder.tvPhoneNumberMana.setText("Phone number: " + list.get(position).getPhoneNumber());
        holder.tvPasswordMana.setText("Password: " + list.get(position).getPassword());

        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        String loggedInUsername = sharedPreferences.getString("USERNAME", "");

        holder.imgDeleteMana.setOnClickListener(v -> {
            showDeleteDialog(position, loggedInUsername);
        });
        holder.itemView.setOnLongClickListener(v -> {
            showUpdateDialog(position);
            return true;
        });
    }

    private void showUpdateDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_user, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        edIdUser = dialogView.findViewById(R.id.edIdUser);
        edUsernameUser = dialogView.findViewById(R.id.edUsernameUser);
        edFullnameUser = dialogView.findViewById(R.id.edFullnameUser);
        edEmailUser = dialogView.findViewById(R.id.edEmailUser);
        edPhoneNumberUser = dialogView.findViewById(R.id.edPhoneNumberUser);
        edPasswordUser = dialogView.findViewById(R.id.edPasswordUser);

        edIdUser.setEnabled(false);
        edUsernameUser.setEnabled(false);

        edIdUser.setText(String.valueOf(list.get(position).getId_user()));
        edUsernameUser.setText(list.get(position).getUsername());
        edFullnameUser.setText(list.get(position).getFullname());
        edEmailUser.setText(list.get(position).getEmail());
        edPhoneNumberUser.setText(list.get(position).getPhoneNumber());
        edPasswordUser.setText(list.get(position).getPassword());

        dialogView.findViewById(R.id.btnSaveUser).setOnClickListener(v -> {
            String fullname = edFullnameUser.getText().toString().trim();
            String email = edEmailUser.getText().toString().trim();
            String phoneNumberStr = edPhoneNumberUser.getText().toString().trim();
            String password = edPasswordUser.getText().toString().trim();

            if (fullname.isEmpty() || email.isEmpty() || phoneNumberStr.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please enter complete information", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int phoneNumber = Integer.parseInt(phoneNumberStr);

                    if (!isValidEmail(email)) {
                        Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (phoneNumber < 1000000 || phoneNumber > 99999999999L) {
                        Toast.makeText(context, "Phone number must be between 7 and 11 digits", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (password.length() < 6) {
                        Toast.makeText(context, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Invalid phone number. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = list.get(position);
                user.setFullname(fullname);
                user.setEmail(email);
                user.setPhoneNumber(phoneNumberStr);
                user.setPassword(password);
                try {
                    if (userDAO.update(user)) {
                        Toast.makeText(context, "Edited successfully", Toast.LENGTH_SHORT).show();
                        list.set(position, user);
                        notifyDataSetChanged();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(context, "Edit failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Edit failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogView.findViewById(R.id.btnCancelUser).setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return email.matches(emailPattern);
    }

    public void showDeleteDialog(int position, String loggedInUsername) {
        User user = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_warning);
        builder.setTitle("Thông báo");

        if (user.getUsername().equals(loggedInUsername)) {
            builder.setMessage("Bạn không thể xóa tài khoản của chính mình.");
            builder.setPositiveButton("OK", null);
        } else {
            builder.setMessage("Bạn có chắc chắn muốn xóa thủ thư " + user.getFullname() + " không?");
            builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (userDAO.delete(user)) {
                            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                            list.remove(user);
                            notifyItemChanged(position);
                            notifyItemRemoved(position);
                        } else {
                            Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tvIdUserMana, tvUsernameMana, tvFullnameMana, tvEmailMana, tvPhoneNumberMana, tvPasswordMana;
        ImageView imgDeleteMana;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdUserMana = itemView.findViewById(R.id.tvIdUserMana);
            tvUsernameMana = itemView.findViewById(R.id.tvUsernameMana);
            tvFullnameMana = itemView.findViewById(R.id.tvFullnameMana);
            tvEmailMana = itemView.findViewById(R.id.tvEmailMana);
            tvPhoneNumberMana = itemView.findViewById(R.id.tvPhoneNumberMana);
            tvPasswordMana = itemView.findViewById(R.id.tvPasswordMana);
            imgDeleteMana = itemView.findViewById(R.id.imgDeleteMana);
        }
    }
}

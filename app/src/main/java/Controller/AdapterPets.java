package Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lucas.adocaoapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import Model.Pets;

public class AdapterPets extends RecyclerView.Adapter<AdapterPets.MyViewHolder> {

    private List<Pets> petsList;
    private Context context;

    public AdapterPets(List<Pets> pets, Context context) {
        this.petsList = pets;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_meuspets, parent, false);
        return new MyViewHolder(item) ;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterPets.MyViewHolder holder, int position) {
        Pets pets = petsList.get(position);
        holder.nome.setText(pets.getNome());
        holder.raca.setText(pets.getRaca());

        //Pegar 1 imagem da lista
        List<String> urlFotos = pets.getFotos();
        String urlCapa = urlFotos.get(0);

        Picasso.get().load(urlCapa).into(holder.imagem);

    }

    @Override
    public int getItemCount() {
        return petsList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imagem;
        TextView nome, raca;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.txtNomeCard);
            raca = itemView.findViewById(R.id.txtRacaCard);
            imagem = itemView.findViewById(R.id.imgAnuncio);

        }
    }

}

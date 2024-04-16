package com.example.and101_capstone.ui.home
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.recyclerview.widget.RecyclerView
//import com.squareup.picasso.Picasso
//
//data class Character(
//    val hair: String,
//    val skin_tone: String,
//    val face: String,
//)
//
//class CharacterAdapter(private val characterList: List<Character>) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {
//
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val characterImage: ImageView = view.findViewById(R.id.imageView)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.character_item_layout, parent, false)
//
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        // Load image using Picasso
//        Picasso.get().load("res/mipmap-anydpi-v26/hair_1.xml").into(holder.characterImage)
//    }
//
//    override fun getItemCount() = characterList.size
//}

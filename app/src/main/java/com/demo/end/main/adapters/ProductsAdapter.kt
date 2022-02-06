package  com.demo.end.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.end.R
import com.demo.end.foundatiion.mvi.BaseAdapter
import com.demo.end.main.model.Product
import kotlinx.android.synthetic.main.product_item.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ProductsAdapter(var context: Context,
                      private var  products: MutableList<Product>
) : BaseAdapter(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ViewHolder(view)
    }

    private var productImage:String=""
    override fun onBindViewHolder(holder1: RecyclerView.ViewHolder, position: Int) {
        val holder=holder1 as ViewHolder
        productImage =products[position].image
        Glide.with(context)
            .load(productImage)
            .fitCenter()
            .into(holder.mProductImage)
        holder.mProductName.text=products[position].name
        holder.mProductPrice.text=products[position].price
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var mProductImage: ImageView = itemView.productImage
        internal var mProductName: TextView = itemView.productName
        internal var mProductPrice: TextView = itemView.productPrice

    }
    fun setProducts(products: List<Product>){
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }
}
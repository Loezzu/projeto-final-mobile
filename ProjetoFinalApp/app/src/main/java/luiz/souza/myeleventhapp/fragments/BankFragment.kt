package luiz.souza.myeleventhapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import luiz.koster.myeleventhapp.R
import luiz.souza.myeleventhapp.adapters.MoneyAdapter
import luiz.souza.myeleventhapp.extensions.toast
import luiz.souza.myeleventhapp.model.Money
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_bank.view.*

class BankFragment : Fragment() {


    private var mAuth: FirebaseAuth? = null
    val database = FirebaseFirestore.getInstance()
    private lateinit var adapter: MoneyAdapter
    private var moneys = arrayListOf<Money>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_bank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.buttonAdd).setOnClickListener {
            findNavController().navigate(R.id.action_bankFragment_to_addFragment)
        }

        view.findViewById<Button>(R.id.buttonAbout).setOnClickListener {
            findNavController().navigate(R.id.action_bankFragment_to_aboutFragment)
        }

        view.findViewById<Button>(R.id.buttonLogout).setOnClickListener {
            mAuth?.signOut()
            findNavController().navigate(R.id.action_bankFragment_to_mainActivity)
        }

        mAuth = FirebaseAuth.getInstance()


        adapter = MoneyAdapter(moneys, requireActivity().applicationContext, {money: Money -> itemClicked(money)})

        view.listMoneyRecyclerView.adapter = adapter

        //layout da recycler
        view.listMoneyRecyclerView.layoutManager = LinearLayoutManager(requireActivity().applicationContext)



        database.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    if(result.isEmpty) {
                        toast("não há dinheiro cadastrado")
                        return@addOnSuccessListener
                    }

                    for(document in result) {
                        var user = document.toObject(Money::class.java)
                        user.key = document.id

                        moneys.add(user)

                    }

                    adapter.updateList()

                }
                .addOnFailureListener { error ->
                    toast("erro ao consultar o valor ${error.toString()}")
                }
    }

    fun itemClicked(money: Money) {
        toast("clicou")
    }

}
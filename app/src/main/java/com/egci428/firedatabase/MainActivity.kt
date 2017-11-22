package com.egci428.firedatabase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var dataReference: DatabaseReference
    lateinit var msgList: MutableList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataReference = FirebaseDatabase.getInstance().getReference("dataMsg")
        msgList = mutableListOf()

        saveBtn.setOnClickListener {
            saveData()
        }

        dataReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.exists()) {
                    msgList.clear()
                    for (i in p0.children) {
                        val message = i.getValue(Message::class.java)
                        msgList.add(message!!)
                    }
                    val adapter = MessageAdapter(applicationContext, R.layout.message, msgList)
                    listView.adapter = adapter
                }
            }
        })
    }

    private fun saveData(){
        val msg = editText.text.toString()
        if(msg.isEmpty()){
            editText.error = "please enter something"
            return
        }
        val dataReference = FirebaseDatabase.getInstance().getReference("dataMsg")
        val messageId = dataReference.push().key
        val messageData = Message(messageId, msg, ratingBar.rating.toInt())
        dataReference.child(messageId).setValue(messageData).addOnCompleteListener {
            Toast.makeText(applicationContext, "message saved", Toast.LENGTH_SHORT).show()
        }
    }

}

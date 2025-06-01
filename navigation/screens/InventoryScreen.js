import * as React from 'react';
import {
  View,
  Text,
  Button,
  StyleSheet,
  TouchableOpacity,
  Modal,
  TextInput,
} from "react-native";
import { useState } from "react";

export default function InventoryScreen({navigation}) {
  const [popupVisibility, setPopupVisibility] = React.useState(false);

  const [text, onChangeText] = React.useState('');

  return (
    <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
      {/* if (getDB().size === 0) {
          alert('No items in inventory. Please add items to your inventory.')
          } */}
      <Text>Please add an ingredient to your inventory</Text>

      <TouchableOpacity
        onPress={() => setPopupVisibility(true)}
        style={styles.roundBtn}
      ></TouchableOpacity>

      <Modal
        transparent={true}
        visible={popupVisibility}
        onRequestClose={() => setPopupVisibility(false)}
        animationType="slide"
      >
        <View style={styles.popupContainer}>
          <View style={styles.popup}>
            <Text>Add an ingredient</Text>
            <TextInput 
            style={styles.input}
          onChangeText={onChangeText}
          value={text}/>
          <View style={styles.buttonContainer}>
            <Button
              title="Close"
              onPress={() => setPopupVisibility(false)}
            ></Button><Button
  title="Add"/></View>
          </View>
        </View>
      </Modal>
    </View>
  );
}


const styles = StyleSheet.create({
  roundBtn: {
    height: 50,
    width: 50,
    padding: 10,
    backgroundColor: "#007BFF",
    borderRadius: 25,
    marginTop: 20,
  },

  popupContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },

  popup: {
    width: "80%",
    height: "40%",
    backgroundColor: "lightblue",
    borderRadius: 10,
    padding: 20,
    justifyContent: "center",
    alignItems: "center",
  },

  input: {
    height: "30%",
    width: "90%",
    margin: 12,
    borderWidth: 1,
    padding: 10,
    color : "black",
    backgroundColor: "white",
  },

  buttonContainer: {
    flexDirection: "column",
    width: "100%",
    marginTop: 20,
  },
});

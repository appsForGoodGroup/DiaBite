import * as React from 'react';
import { View, Text, StyleSheet } from 'react-native';

export default function InventoryScreen({navigation}) {
  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
        <Text  onPress={()=> navigation.navigate('Calendar')}  
        style={{fontSize: 26}}>Inventory Page</Text>
    </View>
  );
}
import * as React from 'react';
import { View, Text, StyleSheet } from 'react-native';
export default function MealPlanScreen({navigation}) {
  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
        <Text  onPress={()=> navigation.navigate('Calendar')}  
        style={{fontSize: 26}}>Meal Plan Page</Text>
    </View>
  );
}
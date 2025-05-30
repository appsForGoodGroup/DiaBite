import * as React from 'react';
import { View, Text, StyleSheet } from 'react-native';

export default function CalendarScreen({naviagation}) {
  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
        <Text onPress ={()=> alert('Calendar Page Pressed!')}
        style={{fontSize: 26}}>This is the Calendar Page</Text>
    </View>
  );
}
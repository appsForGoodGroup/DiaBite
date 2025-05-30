import * as React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import IonIcons from 'react-native-vector-icons/Ionicons';

//Screens
import MealPlanScreen from './screens/MealPlanScreen';
import CalendarScreen from './screens/CalendarScreen';
import InventoryScreen from './screens/InventoryScreen';
//Screen names
const CalendarName = 'Calendar';
const MealPlanName = 'Meal Plan';
const InventoryName = 'Inventory';

const Tab = createBottomTabNavigator();

export default function MainContainer(){
    return(
       <NavigationContainer>
        <Tab.Navigator initialRouteName = {CalendarName} screenOptions={({route})=> ({
            tabBarIcon: ({focused, color, size}) => {
                let iconName;
                let rn = route.name;

                if (rn === CalendarName){
                    iconName = focused ? 'calendar' : 'calendar-outline';
                } else if (rn === MealPlanName) {
                    iconName = focused ? 'restaurant' : 'restaurant-outline';
                } else if (rn === InventoryName) {
                    iconName = focused ? 'list' : 'list-outline';
                }

                return <IonIcons name={iconName} size={size} color={color} />;
            }
        })}>
            <Tab.Screen name={CalendarName} component={CalendarScreen} />
            <Tab.Screen name={MealPlanName} component={MealPlanScreen} />
            <Tab.Screen name={InventoryName} component={InventoryScreen} />

        </Tab.Navigator>
       </NavigationContainer>
    )
}
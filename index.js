import React from 'react';
import {AppRegistry, StyleSheet, Text, View, NativeModules, TouchableOpacity, requireNativeComponent} from 'react-native';

const BulbView = requireNativeComponent("BulbView")

class HelloWorld extends React.Component {
  constructor(props) {
    super(props)
    this.state ={
      bulbStatus: 1,
      isOn: false
    }
  }
  getBulbStatus = () => {
    NativeModules.Bulb.getStatus((data, value) => this.setState({ bulbStatus: value }))
  }
  turnBulbStatusOn = () => {
    NativeModules.Bulb.turnOn()
  }
  turnBulbStatusOff = () => {
    NativeModules.Bulb.turnOff()
  }
  _onStatusChange = e => {
    console.log('the button is==>', e.nativeEvent.isOn)
    this.setState({ isOn: e.nativeEvent.isOn});
  }
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.hello}>Hello, World</Text>
        <Text>The bulb status is - {this.state.bulbStatus.toString()}</Text>
        <TouchableOpacity onPress={this.getBulbStatus}><Text>check bulb status</Text></TouchableOpacity>
        <TouchableOpacity onPress={this.turnBulbStatusOn}><Text>Change bulb status to on</Text></TouchableOpacity>
        <TouchableOpacity onPress={this.turnBulbStatusOff}><Text>Change bulb status to off</Text></TouchableOpacity>
        <BulbView style={ styles.bottom } isOn={true} onStatusChange={this._onStatusChange} />
      </View>
    );
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  bottom: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    },
});

AppRegistry.registerComponent('MyReactNativeApp', () => HelloWorld);

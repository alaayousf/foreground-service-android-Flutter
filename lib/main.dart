import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';


void main() { 
  runApp(const MyApp());
}


//  في حال تم اغالاق الServis ادا كان هناك مكونم من مكونات الاندرود متصل بي الSevise فان ال boundSerivs لا يتم اغلاقة

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();

}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  String token="lpalpapla";


  MethodChannel paltfolme = MethodChannel('backgroundservices');

 
  void stareService() async{
    //her we pass data to code native 
    // startService:name method
    //argumin:Token
     await paltfolme.invokeMethod('startService',{"Token":"$token"});  
  }



  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
             ElevatedButton(onPressed: (){
              log("message");
              stareService();
              
            },
            child:const Text('stareService')),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headline4,
            ),
          ],
        ),
      ),
    
    );
  }
}

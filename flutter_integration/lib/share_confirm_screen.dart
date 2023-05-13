import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_integration/constants.dart';
import 'package:social_share/social_share.dart';

class ShareConfirmScreen extends StatefulWidget {
  const ShareConfirmScreen({Key? key}) : super(key: key);

  @override
  State<ShareConfirmScreen> createState() => _ShareConfirmScreenState();
}

class _ShareConfirmScreenState extends State<ShareConfirmScreen> {
  String? _localImageUrl;

  @override
  void initState() {
    super.initState();
    _fetchImageUrl();
  }

  Future<void> _fetchImageUrl() async {
    const platform = MethodChannel('channel.common');

    try {
      final String result =
          await platform.invokeMethod('getImagePath', "img.png");
      print("Success: $result");
      _updateLocalImageUrl(result);
    } on PlatformException catch (e) {
      print("Failed '${e.message}'.");
    }
  }

  void _updateLocalImageUrl(String url) {
    setState(() {
      _localImageUrl = url;
    });
  }

  Widget _renderContent() {
    if (_localImageUrl != null) {
      return Image.file(
        File(_localImageUrl!),
        fit: BoxFit.cover,
      );
    }
    return const Placeholder();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.white,
      constraints: const BoxConstraints.expand(
          width: double.infinity, height: double.infinity),
      child: _renderContent(),
    );
  }
}

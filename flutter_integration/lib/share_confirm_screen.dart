import 'dart:io';
import 'dart:async';
import 'dart:core';

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
  StreamSubscription<dynamic>? eventSubscription = null;

  @override
  void initState() {
    super.initState();
    _fetchImageUrl();

    const eventChannel = EventChannel('new_image_saved');
    eventSubscription = eventChannel.receiveBroadcastStream().listen(
          (eventData) {
            setState(() {
              _localImageUrl = null;
            });
        _fetchImageUrl();
      },
      onError: (error) {},
      cancelOnError: false,
    );
  }

  @override
  void dispose() {
    eventSubscription?.cancel();
    super.dispose();
  }

  void _doShare(String content, String imagePath) {
    SocialShare.shareOptions(content, imagePath: imagePath);
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

  Widget _renderImage(double screenWidth) {
    ImageProvider? localImage =
    _localImageUrl != null ? FileImage(File(_localImageUrl!)) : null;

    const ImageProvider placeHolderImage = NetworkImage(
        "https://www.genius100visions.com/wp-content/uploads/2017/09/placeholder-vertical.jpg");

    return Container(
      key: UniqueKey(),
      width: screenWidth,
      height: 300,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(8.0),
        image: DecorationImage(
          image: localImage ?? placeHolderImage,
          fit: BoxFit.fitHeight,
          alignment: Alignment.center,
        ),
      ),
    );
  }

  Widget _renderTitle() {
    return const Text(
      "Share it!",
      style: TextStyle(
          fontWeight: FontWeight.bold, fontSize: 24, color: Colors.black87),
    );
  }

  Widget _renderContent() {
    return const Text(
      "If you're happy with \nyour hair cut, share it with us \nand spread the joy",
      style: TextStyle(
          fontWeight: FontWeight.normal, fontSize: 16, color: Colors.black54),
      textAlign: TextAlign.center,
      maxLines: 5,
    );
  }

  Widget _renderCTA() {
    return ElevatedButton(
      onPressed: () {
        _doShare("", _localImageUrl ?? "");
      },
      style: ElevatedButton.styleFrom(
          backgroundColor: const Color(0xFF4b57a9),
          fixedSize: const Size(200, 32)
      ),
      child: const Text('Share'),
    );
  }

  @override
  Widget build(BuildContext context) {
    var size = MediaQuery
        .of(context)
        .size;
    double screenHeight = size.height;
    double screenWidth = size.width;

    return Container(
      color: Colors.white,
      width: screenWidth,
      height: screenHeight,
      child: Wrap(
        spacing: 8,
        crossAxisAlignment: WrapCrossAlignment.center,
        direction: Axis.vertical,
        children: [
          _renderImage(screenWidth),
          _renderTitle(),
          _renderContent(),
          _renderCTA()
        ],
      ),
    );
  }
}

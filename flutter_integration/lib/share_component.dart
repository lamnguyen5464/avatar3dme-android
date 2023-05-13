import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_integration/constants.dart';
import 'package:social_share/social_share.dart';

class ShareItem {
  String shareType;
  String iconUrl;
  ShareAction shareAction;

  ShareItem(this.shareType, this.iconUrl, this.shareAction);
}

abstract class ShareAction {
  void doShare();
}

class ShareFBAction extends ShareAction {
  @override
  Future<void> doShare() async {
    print("Share FB");
    SocialShare.shareOptions("Hello world");
    const platform = MethodChannel('channel.common');

    try {
      final String result = await platform.invokeMethod('getImagePath', "img.png");
      print("Success: $result");
    } on PlatformException catch (e) {
      print("Failed '${e.message}'.");
    }
  }
}

class ShareComponent extends StatelessWidget {
  ShareComponent({super.key});

  final items = [
    ShareItem("facebook", iconFb, ShareFBAction()),
    ShareItem("instagram", iconIg, ShareFBAction()),
    ShareItem("fb", iconFb, ShareFBAction()),
  ];

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.only(
          topLeft: Radius.circular(8),
          topRight: Radius.circular(8),
        ),
      ),
      constraints:
          const BoxConstraints.expand(width: double.infinity, height: double.infinity),
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
          const Text(
            "Share it!",
            style: TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 24,
                color: Colors.black87),
          ),
          const SizedBox(height: 16),
          Wrap(
            // List share item
            spacing: 16,
            children: items.map((item) {
              return Wrap(
                direction: Axis.vertical,
                crossAxisAlignment: WrapCrossAlignment.center,
                spacing: 4,
                children: [
                  IconButton(
                    onPressed: () {
                      item.shareAction.doShare();
                    },
                    icon: Image.network(
                      item.iconUrl,
                      height: 32,
                      width: 32,
                    ),
                  ),
                  Text(
                    item.shareType,
                    style: const TextStyle(fontSize: 12, color: Colors.black87),
                  )
                ],
              );
            }).toList(),
          ),
        ]),
      ),
    );
  }
}

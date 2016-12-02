# Circular-Tab-Indicator

A library to help you create tab indicators and can be easily used with ViewPager.

## Usage

XML
```xml
<com.mondo.circulartabindicator.CircularTabIndicator
        android:id="@+id/tab_indicator"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_centerInParent="true"
        android:layout_marginBottom="16dp"
        app:cti_not_selected_color="@color/not_selected_color"
        app:cti_selected_color="@color/selected_color"
        />
```

Java
```java
ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
viewPager.setAdapter(adapter);

CircularTabIndicator circularTabIndicator = (CircularTabIndicator) findViewById(R.id
    .tab_indicator);
circularTabIndicator.setUpWithViewPager(viewPager);
```

## License

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.

      Included dependencies are:
      Realm (https://github.com/realm/realm-java)

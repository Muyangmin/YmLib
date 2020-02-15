### 0.7.0-SNAPSHOT
#### Ymui
* New widget: `YmuiMultiRippleView`.

#### YmLib
* New delegate class `SharedPreferenceDelegate`.

#### YmKtx
* New collection function `mapOfNonNullValues`.

### 0.6.0
#### New annotation and processor support!
* Generated application with application lifecycle observers
* Support ordered components

### 0.5.0
#### ymktx
* New extend method for `EditText`: `trimmedString()`
* New `XXX.fooCompat` series extend method (equivalent to call `XXXCompat.foo()` methods in official compat library.)
* New `String.splitXXX` extension methods.

#### Ymui
* New feature: `registerEventHook()` for `MutexEventGroup`
* New adapter class: `SimpleTabSelectListener`

### 0.4.1
#### Ymui
* New Feature: `YmuiToast`

#### YmKtx
* New extend method `addAll` for `CircularArray`. 

### 0.4.0
#### Ymui
* New Feature: `MutexViewGroup`
* New utility class `FontSpan`

### 0.3.3
* Fixed: showDividers() function should pass itemCount as second parameter but actually childCount.

### 0.3.2
* Fixed: `YmuiShadowLayout#ymui_shadowSide` XML flag value does not match kotlin constants.

### 0.3.0
* Use compileOnly to avoid potential transitive library issue.

### 0.2.0
First public library version.
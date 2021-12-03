package com.example.kakaomaptest_1.Fragment

//Navigation Drawer(옆에서 스위핑하거나 토글버튼 눌렀을 시 나오는 user, 프로필 설정 drawer)를 enable & disable하는 interface

interface DrawerLocker {
    fun setDrawerEnabled(enabled: Boolean)
}
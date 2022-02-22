package com.example.spyfall.utils


class FragmentNotAttachedException(fragmentName: String) :
    Exception("Fragment not attached to context $fragmentName") {

}

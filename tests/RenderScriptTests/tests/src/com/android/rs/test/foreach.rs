#include "shared.rsh"

int *a;
int dimX;
int dimY;
<<<<<<< HEAD
static bool failed = false;
=======
>>>>>>> upstream/master

void root(int *out, uint32_t x, uint32_t y) {
    *out = x + y * dimX;
}

<<<<<<< HEAD
void foo(const int *in, int *out, uint32_t x, uint32_t y) {
    _RS_ASSERT(*in == (x + y * dimX));
    *out = 99 + x + y * dimX;
    _RS_ASSERT(*out == (99 + x + y * dimX));
}

static bool test_root_output() {
=======
static bool test_foreach_output() {
>>>>>>> upstream/master
    bool failed = false;
    int i, j;

    for (j = 0; j < dimY; j++) {
        for (i = 0; i < dimX; i++) {
            _RS_ASSERT(a[i + j * dimX] == (i + j * dimX));
        }
    }

    if (failed) {
<<<<<<< HEAD
        rsDebug("test_root_output FAILED", 0);
    }
    else {
        rsDebug("test_root_output PASSED", 0);
=======
        rsDebug("test_foreach_output FAILED", 0);
    }
    else {
        rsDebug("test_foreach_output PASSED", 0);
>>>>>>> upstream/master
    }

    return failed;
}

<<<<<<< HEAD
static bool test_foo_output() {
    bool failed = false;
    int i, j;

    for (j = 0; j < dimY; j++) {
        for (i = 0; i < dimX; i++) {
            _RS_ASSERT(a[i + j * dimX] == (99 + i + j * dimX));
        }
    }

    if (failed) {
        rsDebug("test_foo_output FAILED", 0);
    }
    else {
        rsDebug("test_foo_output PASSED", 0);
    }

    return failed;
}

void verify_root() {
    failed |= test_root_output();
}

void verify_foo() {
    failed |= test_foo_output();
}

void foreach_test() {
=======
void foreach_test() {
    bool failed = false;
    failed |= test_foreach_output();

>>>>>>> upstream/master
    if (failed) {
        rsSendToClientBlocking(RS_MSG_TEST_FAILED);
    }
    else {
        rsSendToClientBlocking(RS_MSG_TEST_PASSED);
    }
}


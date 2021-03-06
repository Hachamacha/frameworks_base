/* Copyright 2008 The Android Open Source Project
 */

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <fcntl.h>

#include <private/android_filesystem_config.h>

#include "binder.h"

#if 0
<<<<<<< HEAD
#define ALOGI(x...) fprintf(stderr, "svcmgr: " x)
#define ALOGE(x...) fprintf(stderr, "svcmgr: " x)
=======
#define LOGI(x...) fprintf(stderr, "svcmgr: " x)
#define LOGE(x...) fprintf(stderr, "svcmgr: " x)
>>>>>>> upstream/master
#else
#define LOG_TAG "ServiceManager"
#include <cutils/log.h>
#endif

/* TODO:
 * These should come from a config file or perhaps be
 * based on some namespace rules of some sort (media
 * uid can register media.*, etc)
 */
static struct {
    unsigned uid;
    const char *name;
} allowed[] = {
<<<<<<< HEAD
=======
#ifdef LVMX
    { AID_MEDIA, "com.lifevibes.mx.ipc" },
#endif
>>>>>>> upstream/master
    { AID_MEDIA, "media.audio_flinger" },
    { AID_MEDIA, "media.player" },
    { AID_MEDIA, "media.camera" },
    { AID_MEDIA, "media.audio_policy" },
    { AID_DRM,   "drm.drmManager" },
    { AID_NFC,   "nfc" },
    { AID_RADIO, "radio.phone" },
    { AID_RADIO, "radio.sms" },
    { AID_RADIO, "radio.phonesubinfo" },
    { AID_RADIO, "radio.simphonebook" },
/* TODO: remove after phone services are updated: */
    { AID_RADIO, "phone" },
    { AID_RADIO, "sip" },
    { AID_RADIO, "isms" },
    { AID_RADIO, "iphonesubinfo" },
    { AID_RADIO, "simphonebook" },
<<<<<<< HEAD
    { AID_MEDIA, "common_time.clock" },
    { AID_MEDIA, "common_time.config" },
=======
>>>>>>> upstream/master
};

void *svcmgr_handle;

const char *str8(uint16_t *x)
{
    static char buf[128];
    unsigned max = 127;
    char *p = buf;

    if (x) {
        while (*x && max--) {
            *p++ = *x++;
        }
    }
    *p++ = 0;
    return buf;
}

int str16eq(uint16_t *a, const char *b)
{
    while (*a && *b)
        if (*a++ != *b++) return 0;
    if (*a || *b)
        return 0;
    return 1;
}

int svc_can_register(unsigned uid, uint16_t *name)
{
    unsigned n;
    
    if ((uid == 0) || (uid == AID_SYSTEM))
        return 1;

    for (n = 0; n < sizeof(allowed) / sizeof(allowed[0]); n++)
        if ((uid == allowed[n].uid) && str16eq(name, allowed[n].name))
            return 1;

    return 0;
}

struct svcinfo 
{
    struct svcinfo *next;
    void *ptr;
    struct binder_death death;
<<<<<<< HEAD
    int allow_isolated;
=======
>>>>>>> upstream/master
    unsigned len;
    uint16_t name[0];
};

struct svcinfo *svclist = 0;

struct svcinfo *find_svc(uint16_t *s16, unsigned len)
{
    struct svcinfo *si;

    for (si = svclist; si; si = si->next) {
        if ((len == si->len) &&
            !memcmp(s16, si->name, len * sizeof(uint16_t))) {
            return si;
        }
    }
    return 0;
}

void svcinfo_death(struct binder_state *bs, void *ptr)
{
    struct svcinfo *si = ptr;
<<<<<<< HEAD
    ALOGI("service '%s' died\n", str8(si->name));
=======
    LOGI("service '%s' died\n", str8(si->name));
>>>>>>> upstream/master
    if (si->ptr) {
        binder_release(bs, si->ptr);
        si->ptr = 0;
    }   
}

uint16_t svcmgr_id[] = { 
    'a','n','d','r','o','i','d','.','o','s','.',
    'I','S','e','r','v','i','c','e','M','a','n','a','g','e','r' 
};
  

<<<<<<< HEAD
void *do_find_service(struct binder_state *bs, uint16_t *s, unsigned len, unsigned uid)
=======
void *do_find_service(struct binder_state *bs, uint16_t *s, unsigned len)
>>>>>>> upstream/master
{
    struct svcinfo *si;
    si = find_svc(s, len);

<<<<<<< HEAD
//    ALOGI("check_service('%s') ptr = %p\n", str8(s), si ? si->ptr : 0);
    if (si && si->ptr) {
        if (!si->allow_isolated) {
            // If this service doesn't allow access from isolated processes,
            // then check the uid to see if it is isolated.
            unsigned appid = uid % AID_USER;
            if (appid >= AID_ISOLATED_START && appid <= AID_ISOLATED_END) {
                return 0;
            }
        }
=======
//    LOGI("check_service('%s') ptr = %p\n", str8(s), si ? si->ptr : 0);
    if (si && si->ptr) {
>>>>>>> upstream/master
        return si->ptr;
    } else {
        return 0;
    }
}

int do_add_service(struct binder_state *bs,
                   uint16_t *s, unsigned len,
<<<<<<< HEAD
                   void *ptr, unsigned uid, int allow_isolated)
{
    struct svcinfo *si;
    //ALOGI("add_service('%s',%p,%s) uid=%d\n", str8(s), ptr,
    //        allow_isolated ? "allow_isolated" : "!allow_isolated", uid);
=======
                   void *ptr, unsigned uid)
{
    struct svcinfo *si;
//    LOGI("add_service('%s',%p) uid=%d\n", str8(s), ptr, uid);
>>>>>>> upstream/master

    if (!ptr || (len == 0) || (len > 127))
        return -1;

    if (!svc_can_register(uid, s)) {
<<<<<<< HEAD
        ALOGE("add_service('%s',%p) uid=%d - PERMISSION DENIED\n",
=======
        LOGE("add_service('%s',%p) uid=%d - PERMISSION DENIED\n",
>>>>>>> upstream/master
             str8(s), ptr, uid);
        return -1;
    }

    si = find_svc(s, len);
    if (si) {
        if (si->ptr) {
<<<<<<< HEAD
            ALOGE("add_service('%s',%p) uid=%d - ALREADY REGISTERED, OVERRIDE\n",
=======
            LOGE("add_service('%s',%p) uid=%d - ALREADY REGISTERED, OVERRIDE\n",
>>>>>>> upstream/master
                 str8(s), ptr, uid);
            svcinfo_death(bs, si);
        }
        si->ptr = ptr;
    } else {
        si = malloc(sizeof(*si) + (len + 1) * sizeof(uint16_t));
        if (!si) {
<<<<<<< HEAD
            ALOGE("add_service('%s',%p) uid=%d - OUT OF MEMORY\n",
=======
            LOGE("add_service('%s',%p) uid=%d - OUT OF MEMORY\n",
>>>>>>> upstream/master
                 str8(s), ptr, uid);
            return -1;
        }
        si->ptr = ptr;
        si->len = len;
        memcpy(si->name, s, (len + 1) * sizeof(uint16_t));
        si->name[len] = '\0';
        si->death.func = svcinfo_death;
        si->death.ptr = si;
<<<<<<< HEAD
        si->allow_isolated = allow_isolated;
=======
>>>>>>> upstream/master
        si->next = svclist;
        svclist = si;
    }

    binder_acquire(bs, ptr);
    binder_link_to_death(bs, ptr, &si->death);
    return 0;
}

int svcmgr_handler(struct binder_state *bs,
                   struct binder_txn *txn,
                   struct binder_io *msg,
                   struct binder_io *reply)
{
    struct svcinfo *si;
    uint16_t *s;
    unsigned len;
    void *ptr;
    uint32_t strict_policy;
<<<<<<< HEAD
    int allow_isolated;

//    ALOGI("target=%p code=%d pid=%d uid=%d\n",
=======

//    LOGI("target=%p code=%d pid=%d uid=%d\n",
>>>>>>> upstream/master
//         txn->target, txn->code, txn->sender_pid, txn->sender_euid);

    if (txn->target != svcmgr_handle)
        return -1;

    // Equivalent to Parcel::enforceInterface(), reading the RPC
    // header with the strict mode policy mask and the interface name.
    // Note that we ignore the strict_policy and don't propagate it
    // further (since we do no outbound RPCs anyway).
    strict_policy = bio_get_uint32(msg);
    s = bio_get_string16(msg, &len);
    if ((len != (sizeof(svcmgr_id) / 2)) ||
        memcmp(svcmgr_id, s, sizeof(svcmgr_id))) {
        fprintf(stderr,"invalid id %s\n", str8(s));
        return -1;
    }

    switch(txn->code) {
    case SVC_MGR_GET_SERVICE:
    case SVC_MGR_CHECK_SERVICE:
        s = bio_get_string16(msg, &len);
<<<<<<< HEAD
        ptr = do_find_service(bs, s, len, txn->sender_euid);
=======
        ptr = do_find_service(bs, s, len);
>>>>>>> upstream/master
        if (!ptr)
            break;
        bio_put_ref(reply, ptr);
        return 0;

    case SVC_MGR_ADD_SERVICE:
        s = bio_get_string16(msg, &len);
        ptr = bio_get_ref(msg);
<<<<<<< HEAD
        allow_isolated = bio_get_uint32(msg) ? 1 : 0;
        if (do_add_service(bs, s, len, ptr, txn->sender_euid, allow_isolated))
=======
        if (do_add_service(bs, s, len, ptr, txn->sender_euid))
>>>>>>> upstream/master
            return -1;
        break;

    case SVC_MGR_LIST_SERVICES: {
        unsigned n = bio_get_uint32(msg);

        si = svclist;
        while ((n-- > 0) && si)
            si = si->next;
        if (si) {
            bio_put_string16(reply, si->name);
            return 0;
        }
        return -1;
    }
    default:
<<<<<<< HEAD
        ALOGE("unknown code %d\n", txn->code);
=======
        LOGE("unknown code %d\n", txn->code);
>>>>>>> upstream/master
        return -1;
    }

    bio_put_uint32(reply, 0);
    return 0;
}

int main(int argc, char **argv)
{
    struct binder_state *bs;
    void *svcmgr = BINDER_SERVICE_MANAGER;

    bs = binder_open(128*1024);

    if (binder_become_context_manager(bs)) {
<<<<<<< HEAD
        ALOGE("cannot become context manager (%s)\n", strerror(errno));
=======
        LOGE("cannot become context manager (%s)\n", strerror(errno));
>>>>>>> upstream/master
        return -1;
    }

    svcmgr_handle = svcmgr;
    binder_loop(bs, svcmgr_handler);
    return 0;
}

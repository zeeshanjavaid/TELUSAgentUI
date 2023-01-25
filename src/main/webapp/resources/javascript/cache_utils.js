/*Cache Utils v1*/
var cache_utils = function() {
    return {
        fetchUsingCache: function(e, o, t, a, n) {
            var i, c;
            if ("LocalStorage" === o) {
                if ("undefined" != typeof Storage) {
                    if (void 0 !== (i = window.localStorage.getItem(a))) try {
                        i = JSON.parse(decodeURIComponent(atob(i)))
                    } catch (e) {
                        i = void 0
                    }
                    n.dataSet = i
                }
                if (void 0 === i || null === n.dataSet) return void n.invoke();
                var r;
                if (null !== n.dataSet) {
                    var d = new Date,
                        s = new Date;
                    if (d.setHours(0, 0, 0), s.setHours(23, 59, 59), null == (r = n.dataSet.generationEpoch) || r <= d.getTime() || r >= s.getTime()) return void n.invoke()
                }
                n.dataSet.isCached = !0, console.log(t + "-" + a + " Retrieved from cache. Cache Type:" + o + " Generated: " + new Date(r)), void 0 !== n.onSuccess && (c = (c = n.onSuccess.split("("))[0], n.isCached = !0, "Page" === n.owner && void 0 !== typeof e[c] && e[c](n, i, null), "Prefab" === n.owner && void 0 !== typeof e[c] && e[c](n, i, null))
            }
            "SessionStorage" === o && (null == (i = sessionStorage.getItem(a)) ? n.invoke() : (i = JSON.parse(decodeURIComponent(atob(i))), n.dataSet = i, n.dataSet.isCached = !0, console.log(t + "-" + a + " Retrieved from cache. Cache Type:" + o), void 0 !== n.onSuccess && (c = (c = n.onSuccess.split("("))[0], n.isCached = !0, "Page" === n.owner && void 0 !== typeof e[c] && e[c](n, i, null), "Prefab" === n.owner && void 0 !== typeof e[c] && e[c](n, i, null))))
        },
        storeInCache: function(e, o, t, a) {
            if (void 0 === a || !a.isCached) {
                if ("LocalStorage" === e && "undefined" != typeof Storage) {
                    var n = new Date;
                    a.dataSet.generationEpoch = n.getTime(), window.localStorage.setItem(t, btoa(encodeURIComponent(JSON.stringify(a.dataSet)))), console.log(o + " - " + t + ". Storing to cache. Cache Type:" + e)
                }
                "SessionStorage" === e && (sessionStorage.setItem(t, btoa(encodeURIComponent(JSON.stringify(a.dataSet)))), console.log(o + " - " + t + ". Storing to cache. Cache Type:" + e))
            }
        },
        isStoredInCache: function(e, o, t, a) {
            if ("SessionStorage" === e) {
                return (null === sessionStorage.getItem(t)) ? false : true
            }
        },
        removeFromCache: function(e, o, t, a) {
            if ("SessionStorage" === e) {
                var isAvailableInCache = (null === sessionStorage.getItem(t)) ? false : true;
                if (isAvailableInCache) {
                    sessionStorage.removeItem(t);
                    console.log(o + " - " + t + ". Removing from cache. Cache Type:" + e);
                }
            }
        }
    }
}();
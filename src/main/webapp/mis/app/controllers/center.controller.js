app.controller('CenterController', function ($scope, $http) {
    var tabs = ['#centers', '#zones', '#cities', '#states'];
    $scope.disableSave = false;
    $scope.states = [];

    function debounce(func, wait, immediate) {
        var timeout;
        return function () {
            var context = this, args = arguments;
            var later = function () {
                timeout = null;
                if (!immediate) func.apply(context, args);
            };
            var callNow = immediate && !timeout;
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
            if (callNow) func.apply(context, args);
        };
    }
    $scope.insertedCity = {
        mode: 'New'
    };

    refresh();

    $scope.selectTab = function (index) {
        for (var i = 0; i < tabs.length; i++) {
            var tab = tabs[i];
            if (tab === tabs[index]) {
                $(tab).addClass("active");
            } else {
                $(tab).removeClass("active");
            }
        }
        $scope.editzone = false;
        $scope.editcenter = false;
        $scope.editcity = false;
        $scope.addcenter = false;
    };

    $scope.newCenter = function () {
        $scope.insertedCenter = {
            mode: 'New'
        };
        $scope.editcenter = false;
        $scope.editzone = false;
        $scope.editcity = false;
        $scope.addcenter = true;
    };

    $scope.editCenter = function (center) {
        console.log(center);
        
        $scope.selectedCenter = center;
        $scope.insertedCenter = angular.extend({}, center, center.address);
        $scope.insertedCenter.zone = center.zone ? center.zone.name : '';
        $scope.insertedCenter.mode = 'Edit';
        $scope.editcenter = true;
        $scope.editzone = false;
        $scope.editcity = false;
        console.log($scope.insertedCenter);        
    };

    $scope.saveCenter = function (center) {
        $scope.disableSave=true;
        if (center.id) {
            $http.put('/api/center/', center).then(function () {
                $scope.disableSave=false;
                ok("Center Saved");
                $scope.editcenter = false;
                refresh();
            }, function (response) {
                $scope.disableSave=false;
                error(response.data.error);
            });
        } else {
            $http.post('/api/center/', center).then(function () {
                $scope.disableSave=false;
                ok("Center Saved");
                $scope.editcenter = false;
                refresh();
            }, function (response) {
                $scope.disableSave=false;
                error(response.data.error);
            });
        }
    };
    $scope.cancelCenter = function () {
        $scope.insertedCenter = {
            code: ''
            , capacity: ''
            , name: ''
            , zone: ''
            , type: ''
            , address: ''
            , city: ''
            , state: ''
            , zipcode: ''
            , phone: ''
        };
        $scope.editcenter = false;
        $scope.editcity = false;
        $scope.editzone = false;
        $scope.addcenter = false;
    };

    $scope.removeCenter = function (center) {
        $http.delete('/api/center/' + center.id).then(function () {
            ok("Center Removed");
            refresh();
        }, function (response) {
            error(response.data.error);
        });
    };

    $scope.newZone = function () {
        $scope.insertedZone = {
            mode: 'New'
            , name: ''
            , cities: []
        };
        $scope.editzone = true;
        $scope.editcenter = false;
        $scope.editcity = false;
    };

    $scope.editZone = function (zone) {
        $scope.insertedZone = angular.copy(zone);
        $scope.insertedZone.mode = 'Edit';
        $scope.editzone = true;
        $scope.editcenter = false;
        $scope.editcity = false;
    };

    $scope.saveZone = function (zone) {
        if (zone.id) {
            $http.put('/api/zone/', zone).then(function (response) {
                ok("Zone updated");
                $scope.editzone = false;
                $scope.insertedZone = {};
                refresh();
            }, function (response) {
                error(response.data.error);
            });
        } else {
            $http.post('/api/zone/', zone).then(function (response) {
                ok("Zone Saved");
                $scope.editzone = false;
                $scope.insertedZone = {};
                refresh();
            }, function (response) {
                error(response.data.error);
            });
        }
    };

    $scope.cancelZone = function () {
        $scope.insertedZone = {
            mode: 'New'
            , name: ''
            , cities: []
        };
        $scope.editzone = false;
        $scope.editcenter = false;
        $scope.editcity = false;
    };

    $scope.removeZone = function (zone) {
        $http.delete('/api/zone/' + zone.id).then(function (response) {
            refresh();
            ok("Zone Removed");
        }, function (response) {
            error(response.data.error);
        });
    };

    $scope.newState = function () {
        $scope.insertedState = {
            mode: 'New'
            , name: ''
        };
        $scope.editstate = true;
        $scope.editzone = false;
        $scope.editcenter = false;
        $scope.editcity = false
    };

    $scope.editState = function (state) {
        $scope.insertedState = angular.copy(state);
        $scope.insertedState.mode = 'Edit';
        $scope.editstate = true;
        $scope.editzone = false;
        $scope.editcenter = false;
        $scope.editcity = false;
    }

    $scope.saveState = function (state) {
        if (state.id) {
            $http.put('/api/state/', state).then(function (response) {
                ok("State updated");
                $scope.editstate = false;
                $scope.insertedZone = {};
                refresh();
            }, function (response) {
                error(response.data.error);
            });
        } else {
            $http.post('/api/state/', state).then(function (response) {
                ok("State Saved");
                $scope.editstate = false;
                $scope.insertedZone = {};
                refresh();
            }, function (response) {
                error(response.data.error);
            });
        }
    };

    $scope.removeState = function (state) {
        $http.delete('/api/state/' + state.id).then(function (response) {
            refresh();
            ok("State Removed");
        }, function (response) {
            error(response.data.error);
        });
    };

    $scope.cancelState = function () {
        $scope.insertedState = {
            mode: 'New'
            , name: ''
        };
        $scope.editstate = false;
        $scope.editzone = false;
        $scope.editcenter = false;
        $scope.editcity = false;
    };

    $scope.newCity = function () {
        $scope.insertedCity = {
            mode: 'New'
            , name: ''
            , zone: ''
        };
        $scope.editcity = true;
        $scope.editzone = false;
        $scope.editcenter = false;
        $http.get('/api/city/').then(function (response) {
            $scope.cities = response.data;
        });
    };

    $scope.cancelCity = function () {
        $scope.editcity = false;
        $scope.editzone = false;
        $scope.editcenter = false;
    };

    $scope.saveCity = function (city) {
        if (city.id) {
            $http.put('/api/city/', city).then(function (response) {
                ok("City Updated");
                refresh();
                $scope.insertedCity = {
                    name: ''
                    , zone: ''
                };
            }, function (response) {
                error(response.data.error);
            });
        } else {
            $http.post('/api/city/', city).then(function (response) {
                ok("City Saved");
                refresh();
                $scope.insertedCity = {
                    name: ''
                    , zone: ''
                };
            }, function (response) {
                error(response.data.error);
            });
        }
    };

    $scope.removeCity = function (city) {
        $http.delete('/api/city/' + city.id).then(function (response) {
            ok("City Removed");
            refresh();
        }, function (response) {
            error(response.data.error);
        });
    };

    $scope.editCity = function (city) {
        $scope.editcity = true;
        $scope.editcenter = false;
        $scope.editzone = false;
        $scope.insertedCity = angular.copy(city);
        $scope.insertedCity.mode = "Edit";
    };

    $scope.getState = function(city){
        if($scope.selectedCenter){
            const selectedCity = $scope.selectedCenter.zone.cities.find(element => {
                return element.name === city;
            });
            $scope.insertedCenter.state = selectedCity.state;
        } else {
            const selectedCity = $scope.cities.find(element => {
                return element.name === city;
            });
            $scope.insertedCenter.state = selectedCity.state;
        }


        
    }

    function refresh() {
        $scope.editcenter = false;
        $scope.editzone = false;
        $scope.editcity = false;
        $scope.addcenter = false;
        $http.get('/api/center/').then(function (response) {
            $scope.centers = response.data;
        });
        $http.get('/api/zone/').then(function (response) {
            $scope.zones = response.data;
        });
        $http.get('/api/city/').then(function (response) {
            $scope.cities = response.data;
        });
        $http.get('/api/state/all').then(function (response) {
            $scope.states = response.data;
        });
    }

    function ok(message) {
        swal({
            title: message,
            type: 'success',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-warning"
        });
    }

    function error(message) {
        swal({
            title: message,
            type: 'error',
            buttonsStyling: false,
            confirmButtonClass: "btn btn-warning"
        });
    }
});

app.filter('nameFilter', function () {
    return function (input, name) {
        var out = [];
        if (typeof input === 'object') {
            if (typeof name === 'object' || typeof name === 'string') {
                if (name != null || name != '') {
                    for (var i = 0; i < input.length; i++) {
                        if (input[i].name == name) {
                            out.push(input[i]);
                        }
                    }
                }
            }
        }
        return out;
    }
});
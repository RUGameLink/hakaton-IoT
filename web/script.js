let center = [52.26168772209907,104.26555678587914];

let right_nord = [52.26199807513509,104.26563054662702];
let left_nord = [52.26192483718872,104.26521614533424];

let right_south = [52.261368434432754,104.2654977772808];
let left_south = [52.26145166775148,104.26590413194657];


function init() {
	let map = new ymaps.Map('map-test', {
		center: center,
		zoom: 19
	});

    map.geoObjects.add(new ymaps.Placemark(left_south, {
        balloonContent: 'Привет, я <strong>Северо-Восточный датчик</strong>'
    }, {
        preset: 'islands#circleIcon',
        iconColor: '#3caa3c'
    }))
    .add(new ymaps.Placemark(right_south, {
        balloonContent: 'Привет, я <strong>Юго-Восточный датчик</strong>'
    }, {
        preset: 'islands#circleIcon',
        iconColor: '#3caa3c'
    }))
    .add(new ymaps.Placemark(right_nord, {
        balloonContent: 'Привет, я <strong>Юго-Западный датчик</strong>'
    }, {
        preset: 'islands#circleIcon',
        iconColor: '#3caa3c'
    }))
    .add(new ymaps.Placemark(left_nord, {
        balloonContent: 'Привет, я <strong>Северо-Западный датчик</strong>'
    }, {
        preset: 'islands#circleIcon',
        iconColor: '#3caa3c'
    }))
    .add(new ymaps.Placemark(center, {
        balloonContent: 'Привет, я <strong>Центральный датчик</strong>'
    }, {
        preset: 'islands#circleIcon',
        iconColor: 'blue'
    }));

    map.controls.remove('geolocationControl'); // удаляем геолокацию
    map.controls.remove('searchControl'); // удаляем поиск
    map.controls.remove('trafficControl'); // удаляем контроль трафика

    map.setBounds(polygon.geometry.getBounds());
}

ymaps.ready(init);